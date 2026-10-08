package com.momokotle.agent.utils

import kotlin.random.Random

object ReferralCodeGenerator {
    private const val PREFIX = "MKT"
    private const val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

    fun generate(): String {
        val suffix = buildString {
            repeat(6) {
                append(ALPHABET[Random.nextInt(ALPHABET.length)])
            }
        }
        return "$PREFIX-$suffix"
    }

    fun isValidFormat(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        if (!trimmed.startsWith("$PREFIX-")) return false
        val suffix = trimmed.removePrefix("$PREFIX-")
        return suffix.length == 6 && suffix.all { it in ALPHABET }
    }

    fun normalize(code: String): String = code.trim().uppercase()
}
