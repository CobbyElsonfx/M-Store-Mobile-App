package com.momokotle.agent.sms

import com.momokotle.agent.models.TransactionStatus

/**
 * Detects MoMo Cash Out confirmation SMS bodies.
 * Cash In continues to use on-screen USSD text; Cash Out success is SMS-only.
 */
object CashOutSmsConfirmation {

    fun isSuccess(body: String): Boolean {
        val lower = body.lowercase()
        // Primary: "Cash Out made for GHS 1.00 for DEBORAH SERWAA..."
        if (lower.contains("cash out made")) return true
        // Secondary: cash-out receipt with MoMo financial id
        return lower.contains("cash out") &&
            lower.contains("financial transaction id")
    }

    fun isFailure(body: String): Boolean {
        val lower = body.lowercase()
        if (!lower.contains("cash out")) return false
        return lower.contains("failed") ||
            lower.contains("unsuccessful") ||
            lower.contains("declined") ||
            lower.contains("could not") ||
            lower.contains("insufficient")
    }

    /** Soft amount check so "1" matches "GHS 1.00". */
    fun amountAppearsInBody(body: String, amountHint: String?): Boolean {
        if (amountHint.isNullOrBlank()) return true
        val expected = amountHint.trim().toDoubleOrNull() ?: return true
        val match = Regex("""ghs\s*([\d]+(?:\.\d+)?)""", RegexOption.IGNORE_CASE)
            .find(body)
            ?.groupValues
            ?.getOrNull(1)
            ?.toDoubleOrNull()
            ?: return true
        return kotlin.math.abs(expected - match) < 0.001
    }

    /**
     * Resolves an armed Cash Out SMS into a Room status, or null if the body
     * is unrelated / amount-mismatched.
     */
    fun resolveStatus(body: String, amountHint: String? = null): TransactionStatus? {
        // Failures first — failed receipts can still include Financial Transaction ID.
        if (isFailure(body)) return TransactionStatus.FAILED
        if (isSuccess(body)) {
            return if (amountAppearsInBody(body, amountHint)) {
                TransactionStatus.SUCCESSFUL
            } else {
                null
            }
        }
        return null
    }
}
