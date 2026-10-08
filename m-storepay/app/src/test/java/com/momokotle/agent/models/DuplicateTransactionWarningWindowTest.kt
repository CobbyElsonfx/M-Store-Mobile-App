package com.momokotle.agent.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DuplicateTransactionWarningWindowTest {

    @Test
    fun defaultFromStored_isFiveMinutes() {
        assertEquals(
            DuplicateTransactionWarningWindow.FIVE_MINUTES,
            DuplicateTransactionWarningWindow.fromStored(null)
        )
        assertEquals(
            DuplicateTransactionWarningWindow.FIVE_MINUTES,
            DuplicateTransactionWarningWindow.fromStored("unknown")
        )
    }

    @Test
    fun off_hasNoWindowMs() {
        assertNull(DuplicateTransactionWarningWindow.OFF.windowMs)
    }

    @Test
    fun windows_haveExpectedDurations() {
        assertEquals(60_000L, DuplicateTransactionWarningWindow.ONE_MINUTE.windowMs)
        assertEquals(5 * 60_000L, DuplicateTransactionWarningWindow.FIVE_MINUTES.windowMs)
        assertEquals(30 * 60_000L, DuplicateTransactionWarningWindow.THIRTY_MINUTES.windowMs)
    }
}
