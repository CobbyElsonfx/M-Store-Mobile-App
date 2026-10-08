package com.momokotle.agent.data.remote

import com.google.gson.Gson
import com.momokotle.agent.database.entity.FormSubmissionEntity
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.utils.JsonStringMaps
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FormSubmissionMappingTest {

    private val gson = Gson()

    @Test
    fun jsonStringMapsCoercesNumbersAndIgnoresArrays() {
        val fromObject = JsonStringMaps.fromJson("""{"amount":50,"name":"Ama","ok":true}""")
        assertEquals("50", fromObject["amount"])
        assertEquals("Ama", fromObject["name"])
        assertEquals("true", fromObject["ok"])

        assertTrue(JsonStringMaps.fromJson("[]").isEmpty())
        assertTrue(JsonStringMaps.fromJson("").isEmpty())
        assertTrue(JsonStringMaps.fromJson("null").isEmpty())
        assertTrue(JsonStringMaps.fromJson("{").isEmpty())
    }

    @Test
    fun entityToDomainSurvivesNumericAndArrayJson() {
        val entity = FormSubmissionEntity(
            submissionId = "SUB1",
            userId = "42",
            applicantName = "Ama",
            phoneNumber = "0244123456",
            formType = "Passport Application",
            formDataJson = """{"amount":150,"fullName":"Ama"}""",
            documentsJson = "[]",
            status = "Pending",
            adminNote = null,
            createdAt = 1L,
            updatedAt = 1L
        )

        val domain = entity.toDomain()
        assertNotNull(domain)
        assertEquals(AgentFormType.PASSPORT, domain!!.formType)
        assertEquals("150", domain.formData["amount"])
        assertTrue(domain.uploadedDocuments.isEmpty())
        assertEquals(FormSubmissionStatus.PENDING, domain.status)
    }

    @Test
    fun entityToDomainSkipsUnknownFormTypeAndCorruptJson() {
        val unknown = FormSubmissionEntity(
            submissionId = "SUB2",
            userId = "42",
            applicantName = "Kojo",
            phoneNumber = "0244000000",
            formType = "Unknown Service",
            formDataJson = "{}",
            documentsJson = "{}",
            status = "Approved",
            adminNote = null,
            createdAt = 1L,
            updatedAt = 1L
        )
        assertNull(unknown.toDomain())

        val corrupt = unknown.copy(
            formType = "Agent Card Request",
            formDataJson = "not-json"
        )
        assertNotNull(corrupt.toDomain())
        assertTrue(corrupt.toDomain()!!.formData.isEmpty())
    }

    @Test
    fun mapApplicationAcceptsMixedJsonFromLaravelList() {
        val json = """
            {
              "data": [{
                "id": "SUB202609190001",
                "userId": "7",
                "formType": "passport application",
                "applicantName": "Ama",
                "phoneNumber": "0244123456",
                "formData": {"amount": 50, "fullName": "Ama"},
                "documents": [],
                "status": "Pending",
                "createdAt": "2026-09-19T06:00:00Z",
                "updatedAt": "2026-09-19T06:00:00Z"
              }]
            }
        """.trimIndent()

        val parsed = gson.fromJson(json, ApplicationListResponse::class.java)
        val mapped = LaravelApiMapper.mapApplication(parsed.data.first())

        assertNotNull(mapped)
        assertEquals("SUB202609190001", mapped!!.submissionId)
        assertEquals(AgentFormType.PASSPORT, mapped.formType)
        assertEquals("50", mapped.formData["amount"])
        assertEquals("Ama", mapped.formData["fullName"])
        assertTrue(mapped.uploadedDocuments.isEmpty())
        assertEquals(FormSubmissionStatus.PENDING, mapped.status)
    }

    @Test
    fun fromDisplayNameMatchesLegacyAndCaseInsensitiveNames() {
        assertEquals(AgentFormType.PASSPORT, AgentFormType.fromDisplayName("Passport Application Form"))
        assertEquals(AgentFormType.AGENT_CARD_REQUEST, AgentFormType.fromDisplayName("agent card request"))
        assertEquals(AgentFormType.EVD_SIM_CONVERSION, AgentFormType.fromDisplayName("EVD Agent SIM Conversion"))
        assertNull(AgentFormType.fromDisplayName(" "))
    }
}
