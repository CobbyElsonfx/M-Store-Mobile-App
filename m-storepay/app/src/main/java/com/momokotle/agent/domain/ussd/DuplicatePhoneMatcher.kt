package com.momokotle.agent.domain.ussd

import com.momokotle.agent.utils.PhoneNumberFormatter

/** Builds Room phone variants that should match a normalized Ghana number. */
object DuplicatePhoneMatcher {

    fun candidates(rawOrNormalized: String): List<String> {
        val normalized = PhoneNumberFormatter.normalizeGhanaNumber(rawOrNormalized)
        if (!PhoneNumberFormatter.isValidGhanaNumber(normalized)) return emptyList()
        val national = normalized.removePrefix("0")
        return listOf(
            normalized,
            "233$national",
            "+233$national"
        ).distinct()
    }
}
