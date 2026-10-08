package com.momokotle.agent.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test

class BusinessNameValidatorTest {

    @Test
    fun acceptsValidGhanaianBusinessNames() {
        val validNames = listOf(
            "ADDO 4 Ent.",
            "KOFI 24 VENTURES",
            "A.B.C ENTERPRISE",
            "SMART-LINK ENTERPRISE",
            "K & K VENTURES",
            "ROYAL STAR LTD.",
            "BEST CHOICE ENTERPRISE",
            "ABENA'S COLLECTION",
            "3K ENTERPRISE",
            "1ST CLASS TRADING"
        )
        validNames.forEach { name ->
            assertNull("Expected valid: $name", BusinessNameValidator.validate(name))
        }
    }

    @Test
    fun preservesUserCasing() {
        val input = "ADDO 4 Ent."
        assertEquals("ADDO 4 Ent.", BusinessNameValidator.normalize(input))
    }

    @Test
    fun collapsesMultipleSpaces() {
        assertEquals("K & K VENTURES", BusinessNameValidator.normalize("  K  &   K   VENTURES  "))
    }

    @Test
    fun rejectsMaliciousInput() {
        val invalid = listOf(
            "<script>alert('x')</script>",
            "Business<>Name",
            "javascript:alert(1)",
            "onclick=hack()"
        )
        invalid.forEach { value ->
            assertNotNull(BusinessNameValidator.validate(value))
        }
    }

    @Test
    fun rejectsEmptyAndWhitespaceOnly() {
        assertNotNull(BusinessNameValidator.validate(""))
        assertNotNull(BusinessNameValidator.validate("   "))
    }

    @Test
    fun rejectsTooFewMeaningfulCharacters() {
        assertNotNull(BusinessNameValidator.validate("."))
        assertNotNull(BusinessNameValidator.validate(" & "))
    }

    @Test
    fun filtersDisallowedCharactersOnInput() {
        assertEquals("ADDO 4 Ent.", BusinessNameValidator.filterInput("ADDO 4 Ent.<>"))
    }

    @Test
    fun validateFormDataChecksAllBusinessNameKeys() {
        val formData = mapOf(
            "businessName" to "ADDO 4 Ent.",
            "businessName2" to "K & K VENTURES"
        )
        assertNull(BusinessNameValidator.validateFormData(formData))
    }

    @Test
    fun validateFormDataRejectsInvalidOptionalBusinessName() {
        val formData = mapOf(
            "businessName" to "ADDO 4 Ent.",
            "businessName3" to "<script>bad</script>"
        )
        assertNotNull(BusinessNameValidator.validateFormData(formData))
    }
}
