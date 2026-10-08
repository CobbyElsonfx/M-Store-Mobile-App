package com.momokotle.agent.ussd

data class UssdDialogSnapshot(
    val text: String,
    val hasInputField: Boolean
) {
    val fingerprint: String = normalize(text)
    val looksLikeDialog: Boolean = hasInputField

    companion object {
        private val ignoredLines = setOf(
            "input required",
            "type a message",
            "send",
            "ok",
            "okay",
            "cancel",
            "close",
            "reply",
            "done",
            "submit"
        )

        fun normalize(text: String): String =
            text.lineSequence()
                .map { it.trim() }
                .filter { line ->
                    val lower = line.lowercase()
                    lower.isNotEmpty() && lower !in ignoredLines
                }
                .joinToString("\n")
                .lowercase()
    }
}

sealed class AutomationDecision {
    data object Ignore : AutomationDecision()
    data object Wait : AutomationDecision()
    data class Act(val step: UssdStep, val value: String) : AutomationDecision()
    data object WaitForPin : AutomationDecision()
    data class FinishSuccess(val message: String) : AutomationDecision()
    data class FinishFailure(val message: String) : AutomationDecision()
    /** USSD session is gone; release automation without implying financial success. */
    data object SessionEnded : AutomationDecision()
}
