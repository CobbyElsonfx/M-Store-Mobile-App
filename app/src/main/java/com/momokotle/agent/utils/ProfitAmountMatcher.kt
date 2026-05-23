package com.momokotle.agent.utils

import kotlin.math.abs
import kotlin.math.round

object ProfitAmountMatcher {
    fun normalize(amount: Double): Double = round(amount * 100.0) / 100.0

    fun matches(a: Double, b: Double): Boolean = abs(normalize(a) - normalize(b)) < 0.001
}
