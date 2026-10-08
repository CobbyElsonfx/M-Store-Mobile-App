package com.momokotle.agent.ussd

/**
 * Matches numbered USSD menu lines such as "1. Transfer", "1) MoMo User", or "1 Transfer".
 */
object UssdMenuLineMatcher {

    fun lineMatchesOption(line: String, option: String): Boolean {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || option.isBlank()) return false
        if (trimmed == option) return true
        return menuLineRegex(option).containsMatchIn(trimmed)
    }

    fun menuLineRegex(option: String): Regex {
        val escaped = Regex.escape(option.trim())
        return Regex("""^$escaped(?:[\s.)\-:]|$)""", RegexOption.IGNORE_CASE)
    }

    fun findMatchingLine(screenText: String, option: String): String? =
        screenText.lineSequence()
            .map { it.trim() }
            .firstOrNull { lineMatchesOption(it, option) }
}
