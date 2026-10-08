package com.momokotle.agent.ussd

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
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
            val slotIndex = slotIndexOf(simSlot)
            val subInfo = slotIndex?.let { resolveSubscriptionInfo(it) }
            val handle = if (slotIndex != null) {
                resolvePhoneAccountHandle(slotIndex, subInfo)
            } else {
                null
            }

            if (simSlot == SimSlot.NONE) {
                Log.d(TAG, "Dialing $code without preferred SIM (settings = NONE)")
            }

            val telecomManager = context.getSystemService(TelecomManager::class.java)
            if (handle != null && telecomManager != null) {
                val extras = Bundle().apply {
                    putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                }
                Log.d(
                    TAG,
                    "placeCall $code on $simSlot (slot=$slotIndex, subId=${subInfo?.subscriptionId})"
                )
                telecomManager.placeCall(uri, extras)
                return Result.success(Unit)
            }

            val intent = Intent(Intent.ACTION_CALL).apply {
                data = uri
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (slotIndex != null) {
                applyOemSimExtras(intent, slotIndex, subInfo)
                handle?.let {
                    intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, it)
                }
                Log.w(
                    TAG,
                    "Falling back to ACTION_CALL for $simSlot " +
                        "(handleResolved=${handle != null}, " +
                        "phoneState=${PermissionHelper.hasPhoneStatePermission(context)})"
                )
            }
            context.startActivity(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun applyOemSimExtras(intent: Intent, slotIndex: Int, subInfo: SubscriptionInfo?) {
        intent.putExtra("com.android.phone.extra.slot", slotIndex)
        intent.putExtra("slot", slotIndex)
        intent.putExtra("simSlot", slotIndex)
        intent.putExtra("sim_slot", slotIndex)
        intent.putExtra("phone", slotIndex)
        subInfo?.subscriptionId?.let { subId ->
            intent.putExtra("subscription", subId)
            intent.putExtra("Subscription", subId)
            intent.putExtra("sub_id", subId)
            intent.putExtra("com.android.phone.extra.SUBSCRIPTION_ID", subId)
            intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId)
        }
    }

    private fun slotIndexOf(simSlot: SimSlot): Int? = when (simSlot) {
        SimSlot.SIM1 -> 0
        SimSlot.SIM2 -> 1
        SimSlot.NONE -> null
    }

    @Suppress("MissingPermission")
    private fun resolveSubscriptionInfo(slotIndex: Int): SubscriptionInfo? {
        if (!PermissionHelper.hasPhoneStatePermission(context)) return null
        val subscriptionManager =
            context.getSystemService(SubscriptionManager::class.java) ?: return null
        return try {
            subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(slotIndex)
                ?: subscriptionManager.activeSubscriptionInfoList
                    ?.firstOrNull { it.simSlotIndex == slotIndex }
        } catch (_: SecurityException) {
            null
        }
    }

    @Suppress("MissingPermission")
    private fun resolvePhoneAccountHandle(
        slotIndex: Int,
        subInfo: SubscriptionInfo?
    ): PhoneAccountHandle? {
        val telecomManager = context.getSystemService(TelecomManager::class.java) ?: return null
        val accounts = try {
            telecomManager.callCapablePhoneAccounts
        } catch (_: SecurityException) {
            null
        }.orEmpty()

        if (accounts.isEmpty()) return null

        val subId = subInfo?.subscriptionId
        val iccId = subInfo?.iccId?.takeIf { it.isNotBlank() }

        if (subId != null) {
            accounts.firstOrNull { it.id == subId.toString() }?.let { return it }
            accounts.firstOrNull { it.id.contains(subId.toString()) }?.let { return it }
        }

        if (iccId != null) {
            accounts.firstOrNull { it.id.contains(iccId, ignoreCase = true) }?.let { return it }
        }

        if (subInfo != null) {
            val number = subInfo.number?.filter { it.isDigit() }.orEmpty()
            for (accountHandle in accounts) {
                val account = try {
                    telecomManager.getPhoneAccount(accountHandle)
                } catch (_: SecurityException) {
                    null
                } ?: continue
                val addressDigits = account.address?.schemeSpecificPart
                    ?.filter { it.isDigit() }
                    .orEmpty()
                if (number.isNotEmpty() && addressDigits.isNotEmpty() &&
                    (addressDigits.endsWith(number.takeLast(9)) ||
                        number.endsWith(addressDigits.takeLast(9)))
                ) {
                    return accountHandle
                }
            }
        }

        if (slotIndex in accounts.indices && accounts.size >= 2) {
            return accounts[slotIndex]
        }

        return accounts.getOrNull(slotIndex)
    }

    fun isSimReady(): Boolean {
        val telephony = context.getSystemService(TelephonyManager::class.java)
        return telephony?.simState == TelephonyManager.SIM_STATE_READY
    }

    companion object {
        private const val TAG = "UssdDialHelper"
    }
}
