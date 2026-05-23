package com.momokotle.agent.utils

import com.momokotle.agent.models.TransactionStatus

object TransactionVerificationAnalyzer {

    private val successKeywords = listOf(
        "successful",
        "completed",
        "transaction successful",
        "sent successfully"
    )

    private val failedKeywords = listOf(
        "failed",
        "insufficient balance",
        "declined",
        "cancelled",
        "error"
    )

    private val pendingKeywords = listOf(
        "pending",
        "processing",
        "wait"
    )

    fun analyze(screenText: String): TransactionStatus? {
        if (UssdSecurityHelper.containsSensitiveKeyword(screenText)) return null
        val lower = screenText.lowercase()
        return when {
            successKeywords.any { lower.contains(it) } -> TransactionStatus.SUCCESSFUL
            failedKeywords.any { lower.contains(it) } -> TransactionStatus.FAILED
            pendingKeywords.any { lower.contains(it) } -> TransactionStatus.PENDING
            else -> null
        }
    }
}

object UssdSecurityHelper {
    private val securityKeywords = listOf(
        "pin",
        "password",
        "secret",
        "confirm with pin",
        "enter pin"
    )

    fun containsSensitiveKeyword(text: String): Boolean {
        val lower = text.lowercase()
        return securityKeywords.any { lower.contains(it) }
    }
}
