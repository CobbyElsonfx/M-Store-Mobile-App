package com.momokotle.agent.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.sms.CashOutSmsWatcher
import com.momokotle.agent.ussd.AutomationDecision
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdDialogSnapshot
import com.momokotle.agent.ussd.UssdNodeAutomation
import com.momokotle.agent.ussd.UssdScreenValidator
import com.momokotle.agent.ussd.UssdWindowGate
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.TransactionVerificationAnalyzer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MomoAccessibilityService : AccessibilityService() {

    @Inject lateinit var engine: UssdAutomationEngine
    @Inject lateinit var logger: AutomationLogger
    @Inject lateinit var updateTransactionStatusUseCase: UpdateTransactionStatusUseCase
    @Inject lateinit var accessibilityRepository: AccessibilityRepository
    @Inject lateinit var cashOutSmsWatcher: CashOutSmsWatcher

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.Main.immediate)
    private val handler = Handler(Looper.getMainLooper())

    private var debounceRunnable: Runnable? = null
    private var watchdogRunnable: Runnable? = null
    private var statusWatchJob: Job? = null
    private var actionInProgress = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        accessibilityRepository.refresh()
        logger.log("Accessibility service connected")
        statusWatchJob?.cancel()
        statusWatchJob = serviceScope.launch {
            engine.status.collect { status ->
                if (status.isActive) {
                    ensureWatchdog(engine.currentSessionId)
                } else {
                    releaseAutomation()
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !engine.status.value.isActive) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) {
            return
        }
        val sessionId = engine.currentSessionId
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            engine.noteNewWindow()
        }
        ensureWatchdog(sessionId)
        debounceRunnable?.let { handler.removeCallbacks(it) }
        debounceRunnable = Runnable {
            if (!engine.isCurrentSession(sessionId)) return@Runnable
            processCurrentWindow(sessionId)
        }
        handler.postDelayed(debounceRunnable!!, engine.debounceMs)
    }

    private fun ensureWatchdog(sessionId: Long) {
        if (watchdogRunnable != null) {
            if (!engine.isCurrentSession(sessionId)) {
                stopTimers()
            } else {
                return
            }
        }
        if (!engine.isCurrentSession(sessionId)) return
        watchdogRunnable = object : Runnable {
            override fun run() {
                if (!engine.isCurrentSession(sessionId)) {
                    watchdogRunnable = null
                    actionInProgress = false
                    return
                }
                // Never leave actionInProgress stuck across watchdog ticks.
                if (actionInProgress) {
                    actionInProgress = false
                }
                processCurrentWindow(sessionId)
                if (engine.isCurrentSession(sessionId)) {
                    handler.postDelayed(this, WATCHDOG_MS)
                } else {
                    watchdogRunnable = null
                }
            }
        }
        handler.postDelayed(watchdogRunnable!!, WATCHDOG_MS)
    }

    private fun stopTimers() {
        debounceRunnable?.let { handler.removeCallbacks(it) }
        watchdogRunnable?.let { handler.removeCallbacks(it) }
        debounceRunnable = null
        watchdogRunnable = null
    }

    private fun releaseAutomation() {
        actionInProgress = false
        stopTimers()
    }

    private fun processCurrentWindow(sessionId: Long) {
        if (!engine.isCurrentSession(sessionId) || actionInProgress) return

        val root = rootInActiveWindow ?: run {
            apply(sessionId, engine.evaluate(UssdDialogSnapshot(text = "", hasInputField = false)), "")
            return
        }

        val pkg = root.packageName
        val ussdHost = UssdWindowGate.mayAutomate(pkg)
        // Dashboard Phone/Amount are EditTexts — never treat our own window as a USSD dialog.
        val snapshot = if (ussdHost) {
            UssdDialogSnapshot(
                text = collectText(root),
                hasInputField = UssdNodeAutomation.hasInputField(root)
            )
        } else {
            UssdDialogSnapshot(text = "", hasInputField = false)
        }

        val decision = engine.evaluate(snapshot)
        when (decision) {
            is AutomationDecision.Act -> {
                if (!ussdHost) {
                    root.recycleSafely()
                    return
                }
                executeAct(sessionId, root, decision, snapshot)
            }
            else -> {
                root.recycleSafely()
                apply(sessionId, decision, snapshot.text)
            }
        }
    }

    private fun executeAct(
        sessionId: Long,
        root: AccessibilityNodeInfo,
        decision: AutomationDecision.Act,
        snapshot: UssdDialogSnapshot
    ) {
        if (!engine.isCurrentSession(sessionId)) {
            root.recycleSafely()
            return
        }
        if (!UssdWindowGate.mayAutomate(root.packageName)) {
            root.recycleSafely()
            return
        }
        val step = decision.step
        if (step.actionType == UssdActionType.WAIT) {
            root.recycleSafely()
            if (engine.isCurrentSession(sessionId)) {
                engine.onSubmitted(snapshot.fingerprint, submittedValue = "")
            }
            return
        }
        if (step.actionType == UssdActionType.CLICK) {
            val clicked = performClick(root, step.expectedText)
            root.recycleSafely()
            if (!engine.isCurrentSession(sessionId)) return
            if (clicked) {
                engine.onSubmitted(snapshot.fingerprint, submittedValue = step.expectedText)
            } else {
                engine.onActionFailed()
            }
            if (!engine.status.value.isActive) releaseAutomation()
            return
        }

        actionInProgress = true
        val typed = UssdNodeAutomation.enterValue(this, root, decision.value, logger)
        if (!typed && UssdScreenValidator.isMenuOptionStep(step)) {
            val clicked = UssdNodeAutomation.clickMenuOption(root, decision.value, logger)
            root.recycleSafely()
            actionInProgress = false
            if (!engine.isCurrentSession(sessionId)) return
            if (clicked) {
                engine.onSubmitted(snapshot.fingerprint, submittedValue = decision.value)
            } else {
                engine.onActionFailed()
            }
            if (!engine.status.value.isActive) releaseAutomation()
            return
        }
        root.recycleSafely()

        if (!typed) {
            actionInProgress = false
            if (!engine.isCurrentSession(sessionId)) return
            engine.onActionFailed()
            if (!engine.status.value.isActive) releaseAutomation()
            return
        }

        handler.postDelayed({
            if (!engine.isCurrentSession(sessionId)) {
                actionInProgress = false
                return@postDelayed
            }
            val fresh = rootInActiveWindow
            if (fresh == null || !UssdWindowGate.mayAutomate(fresh.packageName)) {
                fresh.recycleSafely()
                actionInProgress = false
                // Active window left the dialer (often our dashboard) — retry later via watchdog.
                return@postDelayed
            }
            // Re-check field still holds the value before OK — empty submits cause "Input required".
            val fieldOk = run {
                val field = UssdNodeAutomation.findInputField(fresh)
                if (field == null) {
                    true
                } else {
                    val ok = UssdNodeAutomation.fieldShowsValue(field, decision.value) ||
                        decision.value.isBlank()
                    if (!ok) {
                        logger.log("Re-filling USSD field before submit (${decision.value})")
                        UssdNodeAutomation.enterValue(this, fresh, decision.value, logger)
                    }
                    if (field !== fresh) {
                        try {
                            field.recycle()
                        } catch (_: Exception) {
                        }
                    }
                    true
                }
            }
            val submitted = fieldOk && UssdNodeAutomation.clickSubmit(fresh, logger)
            fresh.recycleSafely()
            actionInProgress = false
            if (!engine.isCurrentSession(sessionId)) return@postDelayed
            if (submitted) {
                engine.onSubmitted(snapshot.fingerprint, submittedValue = decision.value)
            } else {
                engine.onActionFailed()
            }
            if (!engine.status.value.isActive) releaseAutomation()
        }, engine.submitSettleMs)
    }

    private fun apply(sessionId: Long, decision: AutomationDecision, screenText: String) {
        when (decision) {
            is AutomationDecision.FinishSuccess -> {
                if (!engine.isCurrentSession(sessionId)) return
                // Cash Out success arrives only via MoMo SMS — never from USSD dialog text.
                if (engine.activeTransactionType == TransactionType.MTN_CASHOUT) {
                    engine.markSuccess("Cash Out submitted — waiting for MoMo SMS confirmation")
                    releaseAutomation()
                    return
                }
                if (decision.message.contains("successfully", ignoreCase = true)) {
                    finalizeFinancialResult(sessionId, TransactionStatus.SUCCESSFUL, screenText)
                } else {
                    engine.markSuccess(decision.message)
                }
                releaseAutomation()
            }
            is AutomationDecision.FinishFailure -> {
                if (!engine.isCurrentSession(sessionId)) return
                val txId = engine.activeTransactionId
                val isCashOut = engine.activeTransactionType == TransactionType.MTN_CASHOUT
                engine.applyDecision(decision)
                if (txId != null) {
                    if (isCashOut) cashOutSmsWatcher.disarm(txId)
                    updateTransactionAsync(txId, TransactionStatus.FAILED)
                }
                releaseAutomation()
            }
            AutomationDecision.SessionEnded -> {
                finalizeSessionEnded(sessionId, screenText)
            }
            else -> {
                if (engine.isCurrentSession(sessionId)) {
                    engine.applyDecision(decision)
                }
            }
        }
    }

    /**
     * Session lifecycle ended. Dialog disappearance alone is never SUCCESSFUL.
     * Short closing screens may still carry a definitive USSD result; long app
     * chrome text is ignored so MoMo Kotle UI cannot flip the DB status.
     * Cash Out: never SUCCESS from USSD — SMS watcher owns success; only FAILED
     * from a clear USSD error screen updates the Room row.
     */
    private fun finalizeSessionEnded(sessionId: Long, screenText: String) {
        if (!engine.isCurrentSession(sessionId)) {
            releaseAutomation()
            return
        }
        val transactionId = engine.activeTransactionId
        val isCashOut = engine.activeTransactionType == TransactionType.MTN_CASHOUT
        val screenStatus = financialStatusFromClosingScreen(screenText)
        engine.markSessionEnded()
        releaseAutomation()
        if (transactionId == null) {
            logger.log("USSD session ended → automation released (no tx)")
            return
        }
        if (isCashOut) {
            when (screenStatus) {
                TransactionStatus.FAILED -> {
                    cashOutSmsWatcher.disarm(transactionId)
                    updateTransactionAsync(transactionId, TransactionStatus.FAILED)
                    logger.log("USSD session ended → Cash Out FAILED on screen (SMS watch disarmed)")
                }
                else -> {
                    // Leave PENDING/ATTEMPTED; CashOutSmsWatcher confirms SUCCESS via SMS.
                    logger.log("USSD session ended → Cash Out awaiting MoMo SMS (tx left unchanged)")
                }
            }
            return
        }
        updateTransactionAsync(transactionId, screenStatus)
        logger.log("USSD session ended → automation released (tx=$screenStatus)")
    }

    private fun financialStatusFromClosingScreen(screenText: String): TransactionStatus {
        if (screenText.length > MAX_USSD_RESULT_CHARS) return TransactionStatus.PENDING
        return when (TransactionVerificationAnalyzer.analyze(screenText)) {
            TransactionStatus.SUCCESSFUL -> TransactionStatus.SUCCESSFUL
            TransactionStatus.FAILED -> TransactionStatus.FAILED
            else -> TransactionStatus.PENDING
        }
    }

    private fun finalizeFinancialResult(
        sessionId: Long,
        status: TransactionStatus,
        screenText: String
    ) {
        if (!engine.isCurrentSession(sessionId)) return
        val transactionId = engine.activeTransactionId
        val resolved = when (TransactionVerificationAnalyzer.analyze(screenText)) {
            TransactionStatus.SUCCESSFUL -> TransactionStatus.SUCCESSFUL
            TransactionStatus.FAILED -> TransactionStatus.FAILED
            else -> status
        }
        if (transactionId != null) {
            updateTransactionAsync(transactionId, resolved)
        }
        when (resolved) {
            TransactionStatus.FAILED -> engine.markFailed("Transaction failed on USSD screen")
            else -> engine.markSuccess("Transaction completed successfully")
        }
    }

    private fun updateTransactionAsync(transactionId: Long, status: TransactionStatus) {
        serviceScope.launch {
            updateTransactionStatusUseCase(transactionId, status)
        }
    }

    private fun performClick(root: AccessibilityNodeInfo, targetText: String?): Boolean {
        if (targetText.isNullOrBlank()) return false
        val node = root.findAccessibilityNodeInfosByText(targetText)?.firstOrNull() ?: return false
        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK).also {
            node.recycleSafely()
        }
    }

    private fun collectText(root: AccessibilityNodeInfo): String {
        val builder = StringBuilder()
        appendTextRecursive(root, builder)
        return builder.toString().trim()
    }

    private fun appendTextRecursive(node: AccessibilityNodeInfo, builder: StringBuilder) {
        node.text?.let { if (it.isNotBlank()) builder.append(it).append('\n') }
        node.hintText?.let { if (it.isNotBlank()) builder.append(it).append('\n') }
        node.contentDescription?.let { if (it.isNotBlank()) builder.append(it).append('\n') }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            appendTextRecursive(child, builder)
            child.recycleSafely()
        }
    }

    private fun AccessibilityNodeInfo?.recycleSafely() {
        try {
            this?.recycle()
        } catch (_: Exception) {
        }
    }

    override fun onInterrupt() {
        logger.log("Accessibility service interrupted")
        accessibilityRepository.refresh()
        engine.cancel()
        releaseAutomation()
    }

    override fun onDestroy() {
        statusWatchJob?.cancel()
        serviceJob.cancel()
        stopTimers()
        super.onDestroy()
    }

    companion object {
        private const val WATCHDOG_MS = 1_000L
        private const val MAX_USSD_RESULT_CHARS = 600
    }
}
