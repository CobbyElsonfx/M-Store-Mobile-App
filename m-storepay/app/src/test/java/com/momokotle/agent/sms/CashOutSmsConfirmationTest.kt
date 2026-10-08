package com.momokotle.agent.sms

import com.momokotle.agent.models.TransactionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CashOutSmsConfirmationTest {

    private val sampleSuccess =
        "Cash Out made for GHS 1.00 for DEBORAH SERWAA." +
            "Current Balance GHS 30.42 Available Balance GHS 30.42." +
            "Financial Transaction ID: 90978353789." +
            "Transaction fee is GHS 0.00. Buy airtime from your MoMo wallet and enjoy 100% bonus."

    @Test
    fun sampleCashOutSms_isSuccess() {
        assertTrue(CashOutSmsConfirmation.isSuccess(sampleSuccess))
    }

    @Test
    fun sampleCashOutSms_resolvesSuccessful() {
        assertEquals(
            TransactionStatus.SUCCESSFUL,
            CashOutSmsConfirmation.resolveStatus(sampleSuccess, amountHint = "1")
        )
        assertEquals(
            TransactionStatus.SUCCESSFUL,
            CashOutSmsConfirmation.resolveStatus(sampleSuccess, amountHint = "1.00")
        )
    }

    @Test
    fun amountHint_matchesGhsDecimal() {
        assertTrue(CashOutSmsConfirmation.amountAppearsInBody(sampleSuccess, "1"))
        assertTrue(CashOutSmsConfirmation.amountAppearsInBody(sampleSuccess, "1.00"))
        assertFalse(CashOutSmsConfirmation.amountAppearsInBody(sampleSuccess, "5"))
    }

    @Test
    fun amountHintMismatch_doesNotResolveSuccess() {
        assertNull(CashOutSmsConfirmation.resolveStatus(sampleSuccess, amountHint = "5"))
    }

    @Test
    fun cashInSms_isNotCashOutSuccess() {
        val cashIn =
            "Receive Cash In made from Agent for GHS 1.00. Financial Transaction ID: 123."
        assertFalse(CashOutSmsConfirmation.isSuccess(cashIn))
        assertNull(CashOutSmsConfirmation.resolveStatus(cashIn))
    }

    @Test
    fun failureSms_resolvesFailed() {
        val body = "Cash Out failed. Insufficient balance. Financial Transaction ID: 1."
        assertTrue(CashOutSmsConfirmation.isFailure(body))
        assertEquals(TransactionStatus.FAILED, CashOutSmsConfirmation.resolveStatus(body))
    }

    @Test
    fun unrelatedSms_ignored() {
        assertNull(
            CashOutSmsConfirmation.resolveStatus("Your MoMo balance is GHS 30.42")
        )
    }
}
