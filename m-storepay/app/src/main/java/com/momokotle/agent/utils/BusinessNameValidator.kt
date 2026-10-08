package com.momokotle.agent.utils

/**
 * Shared business name rules for client and server-side validation.
 * Preserves user-entered casing (as on registration certificates).
 */
object BusinessNameValidator {

    const val MAX_LENGTH = 100
    const val MIN_MEANINGFUL_CHARS = 2

    val BUSINESS_NAME_FIELD_KEYS = setOf(
        "businessName",
        "businessName1",
        "businessName2",
        "businessName3"
    )

    private val DISALLOWED_CHARS = setOf('<', '>', '{', '}', '\\')

    private val UNSAFE_PATTERNS = listOf(
        Regex("<[^>]*>", RegexOption.IGNORE_CASE),
        Regex("javascript:", RegexOption.IGNORE_CASE),
        Regex("on\\w+\\s*=", RegexOption.IGNORE_CASE),
        Regex("data:", RegexOption.IGNORE_CASE)
    )

    fun isAllowedChar(ch: Char): Boolean {
        if (ch in DISALLOWED_CHARS || ch.isISOControl()) return false
        return ch.isLetterOrDigit() || ch in " .'&-/()"
    }

    fun filterInput(raw: String): String =
        raw.filter { isAllowedChar(it) }.take(MAX_LENGTH)

    fun normalize(value: String): String =
        value.trim().replace(Regex("\\s+"), " ")

    fun meaningfulCharacterCount(value: String): Int =
        value.count { it.isLetterOrDigit() }

    fun validate(value: String, label: String = "Business Name"): String? {
        val normalized = normalize(value)
        if (normalized.isBlank()) return "$label is required"
        if (normalized.length > MAX_LENGTH) {
            return "$label must be at most $MAX_LENGTH characters"
        }
        if (meaningfulCharacterCount(normalized) < MIN_MEANINGFUL_CHARS) {
            return "$label must contain at least $MIN_MEANINGFUL_CHARS letters or numbers"
        }
        if (UNSAFE_PATTERNS.any { it.containsMatchIn(normalized) }) {
            return "Enter a valid $label"
        }
        if (normalized.any { !isAllowedChar(it) }) {
            return "Enter a valid $label"
        }
        return null
    }

    fun validateOptional(value: String, label: String): String? {
        if (value.isBlank()) return null
        return validate(value, label)
    }

    /** Validates all business name keys present in submitted form data. */
    fun validateFormData(formData: Map<String, String>): String? {
        BUSINESS_NAME_FIELD_KEYS.forEach { key ->
            val raw = formData[key]?.takeIf { it.isNotBlank() } ?: return@forEach
            validate(raw, labelForKey(key))?.let { return it }
        }
        return null
    }

    fun labelForKey(key: String): String = when (key) {
        "businessName" -> "Business Name"
        "businessName1" -> "Business Name Option 1"
        "businessName2" -> "Business Name Option 2"
        "businessName3" -> "Business Name Option 3"
        else -> "Business Name"
    }
}
