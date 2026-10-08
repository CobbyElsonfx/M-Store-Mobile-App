package com.momokotle.agent.domain.ussd

import java.util.Locale

/**
 * Admin-adjustable large-amount gate before dialing USSD.
 * Default threshold matches the product example (GHS 1,000).
 */
object LargeTransactionWarning {
    const val DEFAULT_THRESHOLD_GHS = 1_000.0

    fun shouldWarn(amount: Double?, thresholdGhs: Double = DEFAULT_THRESHOLD_GHS): Boolean {
        if (amount == null || amount <= 0.0) return false
        val threshold = if (thresholdGhs > 0.0) thresholdGhs else DEFAULT_THRESHOLD_GHS
        return amount >= threshold
    }

    fun formatAmountGhs(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            String.format(Locale.US, "GHS %,d", amount.toLong())
        } else {
            String.format(Locale.US, "GHS %,.2f", amount)
        }
    }
}
