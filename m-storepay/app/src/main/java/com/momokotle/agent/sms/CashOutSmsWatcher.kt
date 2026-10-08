package com.momokotle.agent.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import androidx.core.content.ContextCompat
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.PermissionHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Listens for MoMo Cash Out confirmation SMS and marks the pending Room transaction
 * SUCCESSFUL (with commission) or FAILED.
 */
@Singleton
class CashOutSmsWatcher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updateTransactionStatusUseCase: UpdateTransactionStatusUseCase,
    private val logger: AutomationLogger,
    @ApplicationScope private val scope: CoroutineScope
) {
    data class Pending(
        val transactionId: Long,
        val armedAtMs: Long,
        val amountHint: String?
    )

    private val pending = AtomicReference<Pending?>(null)
    private var receiverRegistered = false
    private var timeoutJob: Job? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            if (intent?.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
            if (!PermissionHelper.hasSmsPermission(context)) return
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
            val body = messages.joinToString(separator = "") { it.displayMessageBody.orEmpty() }
            if (body.isBlank()) return
            handleSmsBody(body)
        }
    }

    fun arm(transactionId: Long, amountHint: String? = null) {
        if (transactionId <= 0L) return
        pending.set(
            Pending(
                transactionId = transactionId,
                armedAtMs = System.currentTimeMillis(),
                amountHint = amountHint
            )
        )
        ensureRegistered()
        scheduleTimeout(transactionId)
        logger.log("Cash Out SMS watch armed for tx=$transactionId")
    }

    /** Call after SMS permission is granted so a pending arm can register the receiver. */
    fun onSmsPermissionGranted() {
        if (pending.get() != null) ensureRegistered()
    }

    fun disarm(transactionId: Long? = null) {
        val current = pending.get()
        if (current == null) return
        if (transactionId != null && current.transactionId != transactionId) return
        pending.compareAndSet(current, null)
        timeoutJob?.cancel()
        timeoutJob = null
        logger.log("Cash Out SMS watch disarmed for tx=${current.transactionId}")
    }

    fun handleSmsBody(body: String) {
        val current = pending.get() ?: return
        if (System.currentTimeMillis() - current.armedAtMs > WATCH_TIMEOUT_MS) {
            pending.compareAndSet(current, null)
            return
        }
        val status = CashOutSmsConfirmation.resolveStatus(body, current.amountHint) ?: run {
            if (CashOutSmsConfirmation.isSuccess(body)) {
                logger.log("Cash Out SMS ignored — amount hint mismatch")
            }
            return
        }
        if (!pending.compareAndSet(current, null)) return
        timeoutJob?.cancel()
        scope.launch {
            updateTransactionStatusUseCase(current.transactionId, status)
            logger.log("Cash Out via SMS → tx=${current.transactionId} $status")
        }
    }

    private fun scheduleTimeout(transactionId: Long) {
        timeoutJob?.cancel()
        timeoutJob = scope.launch {
            delay(WATCH_TIMEOUT_MS)
            val current = pending.get() ?: return@launch
            if (current.transactionId != transactionId) return@launch
            if (pending.compareAndSet(current, null)) {
                logger.log(
                    "Cash Out SMS watch timed out for tx=$transactionId (left PENDING/ATTEMPTED)"
                )
            }
        }
    }

    private fun ensureRegistered() {
        if (receiverRegistered) return
        if (!PermissionHelper.hasSmsPermission(context)) {
            Log.w(TAG, "RECEIVE_SMS not granted — cannot register Cash Out SMS watcher")
            return
        }
        try {
            val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            // System-protected broadcast must be exported to be delivered.
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
            receiverRegistered = true
            logger.log("Cash Out SMS receiver registered")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register SMS receiver", e)
        }
    }

    companion object {
        private const val TAG = "CashOutSmsWatcher"
        /** How long to wait for MoMo Cash Out SMS after arming. */
        const val WATCH_TIMEOUT_MS = 120_000L
    }
}
