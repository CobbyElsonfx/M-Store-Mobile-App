package com.momokotle.agent.utils

object PercentFormatter {
    /** Parses user input like "1", "1.5", or "1%" into decimal rate (0.01, 0.015). */
    fun parsePercentInput(input: String): Double? {
        val cleaned = input.trim().removeSuffix("%").replace(",", "")
        val value = cleaned.toDoubleOrNull() ?: return null
        if (value <= 0 || value > 100) return null
        return value / 100.0
    }

    /** Decimal rate (0.01) to display percent string ("1"). */
    fun toDisplayPercent(decimalRate: Double): String {
        val percent = decimalRate * 100.0
        return if (percent % 1.0 == 0.0) percent.toLong().toString() else percent.toString()
    }

    fun toLabel(decimalRate: Double): String = "${toDisplayPercent(decimalRate)}%"

    fun calculateProfit(amount: Double, decimalRate: Double): Double =
        ProfitAmountMatcher.normalize(amount * decimalRate)
}
