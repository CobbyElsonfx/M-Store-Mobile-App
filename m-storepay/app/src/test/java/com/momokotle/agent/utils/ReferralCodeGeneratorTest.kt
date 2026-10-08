package com.momokotle.agent.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReferralCodeGeneratorTest {

    @Test
    fun generate_producesValidFormat() {
        val code = ReferralCodeGenerator.generate()
        assertTrue(code.startsWith("MKT-"))
        assertTrue(ReferralCodeGenerator.isValidFormat(code))
    }

    @Test
    fun isValidFormat_rejectsInvalidCodes() {
        assertFalse(ReferralCodeGenerator.isValidFormat("ABC-123456"))
        assertFalse(ReferralCodeGenerator.isValidFormat("MKT-123"))
        assertTrue(ReferralCodeGenerator.isValidFormat("MKT-72K3PA"))
    }

    @Test
    fun normalize_uppercasesCode() {
        assertEquals("MKT-72K3PA", ReferralCodeGenerator.normalize("mkt-72k3pa"))
    }
}
