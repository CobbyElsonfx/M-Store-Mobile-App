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
    fun normalizeGhanaNumber(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.startsWith("233") && digits.length >= 12 -> "0${digits.substring(3)}"
            digits.startsWith("0") -> digits
            digits.length == 9 -> "0$digits"
            else -> digits
        }
    }

    fun isValidGhanaNumber(input: String): Boolean {
        val normalized = normalizeGhanaNumber(input)
        return normalized.length == 10 && normalized.startsWith("0")
    }
}

object AmountFormatter {
    fun parse(input: String): Double? =
        input.trim().replace(",", "").toDoubleOrNull()?.takeIf { it > 0 }
}
