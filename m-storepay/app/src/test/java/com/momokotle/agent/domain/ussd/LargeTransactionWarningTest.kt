package com.momokotle.agent.domain.ussd

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LargeTransactionWarningTest {

    @Test
    fun defaultThreshold_warnsAt1000AndAbove() {
        assertFalse(LargeTransactionWarning.shouldWarn(999.99))
        assertTrue(LargeTransactionWarning.shouldWarn(1_000.0))
        assertTrue(LargeTransactionWarning.shouldWarn(2_500.0))
    }

    @Test
    fun adminThreshold_isRespected() {
        assertFalse(LargeTransactionWarning.shouldWarn(1_000.0, thresholdGhs = 2_000.0))
        assertTrue(LargeTransactionWarning.shouldWarn(2_000.0, thresholdGhs = 2_000.0))
    }

    @Test
    fun invalidAmount_doesNotWarn() {
        assertFalse(LargeTransactionWarning.shouldWarn(null))
        assertFalse(LargeTransactionWarning.shouldWarn(0.0))
        assertFalse(LargeTransactionWarning.shouldWarn(-10.0))
    }

    @Test
    fun formatAmountGhs_usesThousandsSeparator() {
        assertEquals("GHS 1,000", LargeTransactionWarning.formatAmountGhs(1_000.0))
        assertEquals("GHS 1,250.50", LargeTransactionWarning.formatAmountGhs(1_250.5))
    }
}
