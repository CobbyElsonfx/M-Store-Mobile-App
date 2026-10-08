package com.momokotle.agent.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class AmountFormatterUssdTest {

    @Test
    fun formatForUssd_wholeNumber_staysClean() {
        assertEquals("10", AmountFormatter.formatForUssd(10.0))
        assertEquals("100", AmountFormatter.formatForUssd(100.0))
    }

    @Test
    fun formatForUssd_preservesDecimals_canonicalRepresentation() {
        // Double 10.50 is 10.5; canonical representation drops trailing zero.
        assertEquals("10.5", AmountFormatter.formatForUssd(10.50))
        assertEquals("25.5", AmountFormatter.formatForUssd(25.5))
        assertEquals("100.75", AmountFormatter.formatForUssd(100.75))
    }

    @Test
    fun sanitizeForUssd_preservesDecimalPoint() {
        assertEquals("10", AmountFormatter.sanitizeForUssd("10"))
        assertEquals("10.50", AmountFormatter.sanitizeForUssd("10.50"))
        assertEquals("25.5", AmountFormatter.sanitizeForUssd("25.5"))
        assertEquals("100.75", AmountFormatter.sanitizeForUssd("100.75"))
    }

    @Test
    fun sanitizeForUssd_stripsCommasAndNonNumericNoise() {
        // Commas are thousands separators (same as AmountFormatter.parse), not decimals.
        assertEquals("1000.50", AmountFormatter.sanitizeForUssd("1,000.50"))
        assertEquals("100.75", AmountFormatter.sanitizeForUssd("GHS 100.75"))
        assertEquals("25.5", AmountFormatter.sanitizeForUssd(" 25.5 "))
    }

    @Test
    fun roundTrip_parseThenFormat_preservesValueForUssd() {
        assertEquals("10", AmountFormatter.formatForUssd(AmountFormatter.parse("10")!!))
        assertEquals("10.5", AmountFormatter.formatForUssd(AmountFormatter.parse("10.50")!!))
        assertEquals("25.5", AmountFormatter.formatForUssd(AmountFormatter.parse("25.5")!!))
        assertEquals("100.75", AmountFormatter.formatForUssd(AmountFormatter.parse("100.75")!!))
    }
}
