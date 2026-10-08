package com.momokotle.agent.ussd

import com.momokotle.agent.models.UssdActionType

object UssdScreenValidator {

    private val fatalErrors = listOf(
        "session expired",
        "service unavailable",
        "unknown subscriber",
        "invalid option",
        "invalid selection",
        "cannot process"
    )

    fun containsFatalError(screenText: String): Boolean {
        val lower = screenText.lowercase()
        return fatalErrors.any { lower.contains(it) }
    }

    fun isMenuOptionStep(step: UssdStep): Boolean {
        if (step.actionType != UssdActionType.INPUT) return false
        val value = step.inputValue.orEmpty()
        return value.isNotEmpty() && value.all { it.isDigit() }
    }

    fun requiresResolvedValue(step: UssdStep): Boolean =
        step.actionType == UssdActionType.INPUT &&
            step.inputValue?.startsWith("{") == true
}
