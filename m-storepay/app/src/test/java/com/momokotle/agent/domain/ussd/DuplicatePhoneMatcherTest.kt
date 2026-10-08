package com.momokotle.agent.domain.ussd

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DuplicatePhoneMatcherTest {

    @Test
    fun candidates_includeLocalAndInternationalForms() {
        val candidates = DuplicatePhoneMatcher.candidates("0241234567")
        assertTrue(candidates.contains("0241234567"))
        assertTrue(candidates.contains("233241234567"))
        assertTrue(candidates.contains("+233241234567"))
    }

    @Test
    fun candidates_normalizePlus233Input() {
        val candidates = DuplicatePhoneMatcher.candidates("+233241234567")
        assertEquals("0241234567", candidates.first())
        assertTrue(candidates.contains("233241234567"))
    }

    @Test
    fun invalidPhone_returnsEmpty() {
        assertTrue(DuplicatePhoneMatcher.candidates("123").isEmpty())
        assertTrue(DuplicatePhoneMatcher.candidates("").isEmpty())
    }
}
