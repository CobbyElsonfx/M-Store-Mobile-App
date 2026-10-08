package com.momokotle.agent.utils

import com.momokotle.agent.models.TransactionStatus

object TransactionVerificationAnalyzer {

    private val successKeywords = listOf(
        "successful",
        "completed",
        "transaction successful",
        "sent successfully",
        // MTN agent Cash In confirmation (on-screen USSD dialog), e.g.
        // "Receive Cash In made from <agent> for GHS <amount>"
        "cash in made",
        "receive cash in"
        // Cash Out success is SMS-only — see CashOutSmsConfirmation, not USSD dialog text.
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
    private val securityPatterns = listOf(
        Regex("""\benter\s+pin\b""", RegexOption.IGNORE_CASE),
        Regex("""\benter\s+your\s+pin\b""", RegexOption.IGNORE_CASE),
        Regex("""\bconfirm\s+with\s+pin\b""", RegexOption.IGNORE_CASE),
        Regex("""\bpin\s+to\s+confirm\b""", RegexOption.IGNORE_CASE),
        Regex("""\bmomo\s+pin\b""", RegexOption.IGNORE_CASE),
        Regex("""\bpassword\b""", RegexOption.IGNORE_CASE),
        Regex("""\bsecret\s+code\b""", RegexOption.IGNORE_CASE),
    )

    fun containsSensitiveKeyword(text: String): Boolean {
        val lower = text.lowercase()
        return securityPatterns.any { it.containsMatchIn(lower) }
    }
}
