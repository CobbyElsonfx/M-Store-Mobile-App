package com.momokotle.agent.ussd

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.utils.PermissionHelper

class UssdDialHelper(
    private val context: Context
) {
    fun dialUssdCode(code: String, simSlot: SimSlot = SimSlot.NONE): Result<Unit> {
        if (!PermissionHelper.hasCallPermission(context)) {
            return Result.failure(IllegalStateException("CALL_PHONE permission not granted"))
        }

        return try {
            val encoded = Uri.encode(code)
            val uri = Uri.parse("tel:$encoded")
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = uri
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            if (simSlot != SimSlot.NONE) {
                resolvePhoneAccountHandle(simSlot)?.let { handle ->
                    intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                }
            }

            context.startActivity(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("MissingPermission")
    private fun resolvePhoneAccountHandle(simSlot: SimSlot): PhoneAccountHandle? {
        val telecomManager = context.getSystemService(TelecomManager::class.java) ?: return null
        val subscriptionManager = context.getSystemService(SubscriptionManager::class.java) ?: return null

        val slotIndex = when (simSlot) {
            SimSlot.SIM1 -> 0
            SimSlot.SIM2 -> 1
            SimSlot.NONE -> return null
        }

        val subscriptions = subscriptionManager.activeSubscriptionInfoList ?: return null
        if (slotIndex >= subscriptions.size) return null

        val subscriptionId = subscriptions[slotIndex].subscriptionId

        return telecomManager.callCapablePhoneAccounts?.firstOrNull { account ->
            account.id.contains(subscriptionId.toString())
        }
    }

    fun isSimReady(): Boolean {
        val telephony = context.getSystemService(TelephonyManager::class.java)
        return telephony?.simState == TelephonyManager.SIM_STATE_READY
    }
}
