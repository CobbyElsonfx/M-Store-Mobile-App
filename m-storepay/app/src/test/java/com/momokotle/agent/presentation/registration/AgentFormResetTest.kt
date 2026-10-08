package com.momokotle.agent.presentation.registration

import com.momokotle.agent.data.form.AgentFormDefinitions
import com.momokotle.agent.models.AgentFormType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AgentFormResetTest {

    @Test
    fun resetFormClearsFieldsUploadsAndValidationForEveryApplicationType() {
        AgentFormType.entries.forEach { type ->
            val definition = AgentFormDefinitions.get(type)
            val filled = AgentFormUiState(
                definition = definition,
                submissionId = "APP-OLD",
                fieldValues = definition.allFields().associate { it.key to "filled-${it.key}" },
                uploads = definition.allUploads().associate { it.key to "https://example.com/${it.key}" },
                uploadFileNames = definition.allUploads().associate { it.key to "${it.key}.jpg" },
                uploadProgress = definition.allUploads().associate { it.key to 100 },
                uploadingSlots = setOf("in-progress"),
                previewPaths = definition.allUploads().associate { it.key to "/tmp/${it.key}" },
                fieldErrors = mapOf("phoneNumber" to "Invalid"),
                fieldValid = definition.allFields().associate { it.key to true },
                uploadErrors = mapOf("ghanaCard" to "Missing"),
                uploadValid = definition.allUploads().associate { it.key to true },
                touchedFields = definition.allFields().map { it.key }.toSet(),
                touchedUploads = definition.allUploads().map { it.key }.toSet(),
                validationAttempted = true,
                currentStepIndex = 2,
                stepValidationAttempted = true,
                isFormValid = true,
                isSubmitting = true,
                message = null,
                errorMessage = "Should be cleared"
            )

            val reset = filled.resetForm("Application submitted successfully.")

            assertEquals(definition, reset.definition)
            assertEquals("Application submitted successfully.", reset.message)
            assertNull(reset.submissionId)
            assertNull(reset.errorMessage)
            assertTrue(reset.fieldValues.isEmpty())
            assertTrue(reset.uploads.isEmpty())
            assertTrue(reset.uploadFileNames.isEmpty())
            assertTrue(reset.uploadProgress.isEmpty())
            assertTrue(reset.uploadingSlots.isEmpty())
            assertTrue(reset.previewPaths.isEmpty())
            assertTrue(reset.fieldErrors.isEmpty())
            assertTrue(reset.fieldValid.isEmpty())
            assertTrue(reset.uploadErrors.isEmpty())
            assertTrue(reset.uploadValid.isEmpty())
            assertTrue(reset.touchedFields.isEmpty())
            assertTrue(reset.touchedUploads.isEmpty())
            assertFalse(reset.validationAttempted)
            assertEquals(0, reset.currentStepIndex)
            assertFalse(reset.stepValidationAttempted)
            assertFalse(reset.isFormValid)
            assertFalse(reset.isSubmitting)
        }
    }

    @Test
    fun failedSubmitCopyKeepsEnteredValues() {
        val definition = AgentFormDefinitions.get(AgentFormType.AGENT_CARD_REQUEST)
        val filled = AgentFormUiState(
            definition = definition,
            submissionId = "APP-1",
            fieldValues = mapOf("firstName" to "Ama", "surname" to "Mensah"),
            uploads = mapOf("ghanaCard" to "https://example.com/card.jpg"),
            uploadFileNames = mapOf("ghanaCard" to "card.jpg"),
            previewPaths = mapOf("ghanaCard" to "/tmp/card.jpg"),
            isSubmitting = true
        )

        val afterFailure = filled.copy(
            isSubmitting = false,
            errorMessage = "Submission failed"
        )

        assertEquals("Ama", afterFailure.fieldValues["firstName"])
        assertEquals("Mensah", afterFailure.fieldValues["surname"])
        assertEquals("https://example.com/card.jpg", afterFailure.uploads["ghanaCard"])
        assertEquals("card.jpg", afterFailure.uploadFileNames["ghanaCard"])
        assertEquals("/tmp/card.jpg", afterFailure.previewPaths["ghanaCard"])
        assertEquals("APP-1", afterFailure.submissionId)
        assertEquals("Submission failed", afterFailure.errorMessage)
        assertFalse(afterFailure.isSubmitting)
    }
}
