package com.momokotle.agent.utils

import com.momokotle.agent.models.TransactionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TransactionVerificationAnalyzerTest {

    @Test
    fun cashInReceipt_isSuccessful() {
        val text = "Receive Cash In made from Francis Agent for GHC 50.00"
        assertEquals(
            TransactionStatus.SUCCESSFUL,
            TransactionVerificationAnalyzer.analyze(text)
        )
    }

    @Test
    fun menuCashInOption_isNotSuccessful() {
        // Must not treat the agent menu line "3. Cash In" as a completed tx.
        assertNull(
            TransactionVerificationAnalyzer.analyze(
                "MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In"
            )
        )
    }

    @Test
    fun cashOutReceipt_notSuccessfulViaUssdAnalyzer() {
        // Cash Out success is SMS-only — USSD screen analyzer must not mark SUCCESSFUL.
        val text =
            "Cash Out made for GHS 10.00 for DEBORAH SERWAA." +
                "Current Balance GHS 180.94 Available Balance GHS 180.94" +
                "Financial Transaction ID: 90287946093." +
                "Transaction fee is GHS 0.00. Buy airtime from your MoMo wallet and enjoy 100% bonus."
        assertNull(TransactionVerificationAnalyzer.analyze(text))
    }

    @Test
    fun menuCashOutOption_isNotSuccessful() {
        assertNull(
            TransactionVerificationAnalyzer.analyze(
                "MTN Agent\n1. Merchant\n2. Cash Out\n3. Cash In"
            )
        )
    }
}
