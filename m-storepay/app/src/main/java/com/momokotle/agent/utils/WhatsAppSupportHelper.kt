package com.momokotle.agent.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Builds official WhatsApp deep links from a Laravel-configured support number.
 * Prefer https://wa.me/{digits}; falls back to api.whatsapp.com/send when needed.
 */
object WhatsAppSupportHelper {

    fun normalizeDigits(raw: String): String? {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return null
        var digits = trimmed.filter { it.isDigit() }
        if (digits.isEmpty()) return null
        if (digits.startsWith("0") && digits.length == 10) {
            digits = "233${digits.substring(1)}"
        }
        if (digits.length !in 8..15) return null
        return digits
    }

    fun chatUrl(digits: String): String = "https://wa.me/$digits"

    fun apiSendUrl(digits: String): String = "https://api.whatsapp.com/send?phone=$digits"

    /**
     * Opens WhatsApp chat for [digits]. Tries the official wa.me deep link first
     * (opens the app when installed, otherwise browser / WhatsApp Web).
     * Returns false only if no handler can open either URI.
     */
    fun openChat(context: Context, digits: String): Boolean {
        val primary = Intent(Intent.ACTION_VIEW, Uri.parse(chatUrl(digits))).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(primary)
            true
        } catch (_: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(apiSendUrl(digits))).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
                true
            } catch (_: ActivityNotFoundException) {
                false
            }
        }
    }
}
