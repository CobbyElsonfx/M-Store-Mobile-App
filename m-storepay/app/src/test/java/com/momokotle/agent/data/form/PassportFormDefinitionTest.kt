package com.momokotle.agent.data.form

import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.utils.FormValidators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PassportFormDefinitionTest {

    private val definition = AgentFormDefinitions.get(AgentFormType.PASSPORT)

    @Test
    fun passportIsSteppedWithSevenStepsIncludingReview() {
        assertTrue(definition.isStepped())
        assertEquals(7, definition.steps.size)
        assertEquals("Applicant Information", definition.steps[0].title)
        assertEquals("Education", definition.steps[1].title)
        assertEquals("Parents Details", definition.steps[2].title)
        assertEquals("Grandparent Details", definition.steps[3].title)
        assertEquals("Guarantors", definition.steps[4].title)
        assertEquals("Witness", definition.steps[5].title)
        assertEquals("Review & Submit", definition.steps[6].title)
        assertTrue(definition.steps.last().isReview)
    }

    @Test
    fun passportIncludesRequiredApplicantAndWitnessFields() {
        val keys = definition.allFields().map { it.key }.toSet()
        listOf(
            "surname", "firstName", "otherName", "gender", "dateOfBirth",
            "cityOfBirth", "countryOfBirth", "height", "nationality",
            "oldPassportNumber", "oldPassportDateOfIssue", "socialSecurityNumber",
            "voterIdNumber", "birthCertificateNumber", "birthCertificateDateOfIssue",
            "birthCertificatePlaceOfIssue", "maritalStatus",
            "newPassportProfession", "oldPassportProfession",
            "phoneNumber", "emailAddress", "houseNumber", "cityOfResidence",
            "houseStreetName", "postOfficeBox",
            "hasFormalEducation", "schoolName", "schoolAddress",
            "fatherFullName", "fatherLivingStatus", "motherFullName", "motherLivingStatus",
            "grandparentFullName", "guarantor1FullName", "guarantor2FullName",
            "witnessFullName", "qualifiedWitness"
        ).forEach { key ->
            assertTrue("$key should be on the passport form", key in keys)
        }
        assertTrue(definition.allUploads().any { it.key == "signature" })
        assertEquals(FormFieldKind.DATE, definition.allFields().first { it.key == "dateOfBirth" }.kind)
        assertEquals(FormFieldKind.PHONE, definition.allFields().first { it.key == "phoneNumber" }.kind)
    }

    @Test
    fun educationDetailsAreHiddenUntilApplicantSelectsYes() {
        val schoolName = definition.allFields().first { it.key == "schoolName" }
        assertFalse(schoolName.isVisible(mapOf("hasFormalEducation" to "No")))
        assertFalse(schoolName.isEffectivelyRequired(mapOf("hasFormalEducation" to "No")))
        assertTrue(schoolName.isVisible(mapOf("hasFormalEducation" to "Yes")))
        assertTrue(schoolName.isEffectivelyRequired(mapOf("hasFormalEducation" to "Yes")))
    }

    @Test
    fun deceasedParentDoesNotRequireTelephoneOrEmail() {
        val fatherPhone = definition.allFields().first { it.key == "fatherTelephone" }
        val values = mapOf("fatherLivingStatus" to "Dead")
        assertTrue(fatherPhone.isVisible(values))
        assertFalse(fatherPhone.isEffectivelyRequired(values))
        assertTrue(fatherPhone.isEffectivelyRequired(mapOf("fatherLivingStatus" to "Living")))
    }

    @Test
    fun existingFormsRemainUnstepped() {
        AgentFormType.entries.filter { it != AgentFormType.PASSPORT }.forEach { type ->
            assertFalse(
                "${type.displayName} should stay a single-page form",
                AgentFormDefinitions.get(type).isStepped()
            )
        }
    }
}

class FormValidatorsEducationTest {

    @Test
    fun educationEndCannotBeBeforeStart() {
        val invalid = mapOf(
            "hasFormalEducation" to "Yes",
            "schoolMonthStarted" to "June",
            "schoolYearStarted" to "2012",
            "schoolMonthEnded" to "March",
            "schoolYearEnded" to "2011"
        )
        assertEquals(
            "Education end date should not be before start date",
            FormValidators.educationDateOrderError(invalid)
        )
    }

    @Test
    fun educationDatesAreSkippedWhenNoFormalEducation() {
        assertNull(
            FormValidators.educationDateOrderError(
                mapOf("hasFormalEducation" to "No", "schoolYearStarted" to "2015", "schoolYearEnded" to "2010")
            )
        )
    }

    @Test
    fun dateOfBirthCannotBeInTheFutureButPassportDoesNotRequireAdultAge() {
        val field = FormTextField("dateOfBirth", "Date of Birth", kind = FormFieldKind.DATE)
        assertNotNull(FormValidators.validateField(field, "01/01/2099"))
        assertNull(FormValidators.validateField(field, "15/03/2015"))
    }

    @Test
    fun namesAllowApostrophesAndHyphens() {
        val field = FormTextField("surname", "Surname", kind = FormFieldKind.NAME)
        val filtered = FormValidators.filterInput(field, "O'Neil-Mensah")
        assertEquals("O'Neil-Mensah", filtered)
        assertNull(FormValidators.validateField(field, filtered))
    }
}
