package com.momokotle.agent.utils

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.ContextCompat

object PermissionHelper {
    fun hasCallPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

    fun hasPhoneStatePermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED

    fun hasSmsPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED

    /** Permissions required to dial USSD on a specific SIM slot. */
    fun hasDialPermissions(context: Context): Boolean =
        hasCallPermission(context) && hasPhoneStatePermission(context)

    val dialPermissions: Array<String> = arrayOf(
        android.Manifest.permission.CALL_PHONE,
        android.Manifest.permission.READ_PHONE_STATE
    )

    /** Required to confirm Cash Out from MoMo SMS (success is not shown as USSD dialog). */
    val smsPermissions: Array<String> = arrayOf(
        android.Manifest.permission.RECEIVE_SMS,
        android.Manifest.permission.READ_SMS
    )

    val cashOutPermissions: Array<String> = dialPermissions + smsPermissions

    fun hasContactsPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

    const val CONTACTS_PERMISSION: String = android.Manifest.permission.READ_CONTACTS
}

object AccessibilityHelper {
    fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<*>): Boolean {
        val expected = "${context.packageName}/${serviceClass.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val splitter = TextUtils.SimpleStringSplitter(':')
        splitter.setString(enabledServices)
        while (splitter.hasNext()) {
            val service = splitter.next()
            if (service.equals(expected, ignoreCase = true)) return true
        }
        return false
    }

    fun openAccessibilitySettings(context: Context) {
        context.startActivity(
            android.content.Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}

object PhoneNumberFormatter {
    private const val MAX_LENGTH = 10

    /** Digits only, capped at 10 — for live phone field input. */
    fun filterInput(input: String): String =
        input.filter { it.isDigit() }.take(MAX_LENGTH)

    fun normalizeGhanaNumber(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.startsWith("233") && digits.length >= 12 -> "0${digits.substring(3)}"
            digits.startsWith("0") -> digits.take(MAX_LENGTH)
            digits.length == 9 -> "0$digits"
            else -> digits.take(MAX_LENGTH)
        }
    }

    fun isValidGhanaNumber(input: String): Boolean {
        val normalized = normalizeGhanaNumber(input)
        return normalized.length == MAX_LENGTH && normalized.startsWith("0")
    }
}

object AmountFormatter {
    fun parse(input: String): Double? =
        input.trim().replace(",", "").toDoubleOrNull()?.takeIf { it > 0 }

    /**
     * Formats a parsed amount for USSD input.
     * Whole numbers stay clean (`10` → `"10"`); decimals keep their fractional part
     * (`10.5` → `"10.5"`, `100.75` → `"100.75"`).
     */
    fun formatForUssd(amount: Double): String {
        if (amount <= 0.0) return ""
        return if (amount % 1.0 == 0.0) {
            amount.toLong().toString()
        } else {
            amount.toString()
        }
    }

    /**
     * Sanitizes an amount string for USSD placeholder substitution.
     * Keeps digits and a single decimal point; strips commas and other noise.
     * Does not strip the decimal point (unlike digit-only phone sanitizing).
     */
    fun sanitizeForUssd(raw: String): String {
        val trimmed = raw.trim().replace(",", "")
        val builder = StringBuilder(trimmed.length)
        var seenDecimal = false
        for (ch in trimmed) {
            when {
                ch.isDigit() -> builder.append(ch)
                ch == '.' && !seenDecimal -> {
                    builder.append(ch)
                    seenDecimal = true
                }
            }
        }
        return builder.toString()
    }
}
