package com.momokotle.agent.models

enum class StoreCategory {
    SIM_CARDS,
    AGENT_MATERIALS
}

enum class AgentFormType {
    AGENT_CARD_REQUEST,
    EVD_SIM_CONVERSION,
    DYNAMIC_SIM_BULK,
    TELECEL_AGENT,
    AIRTELTIGO_AGENT,
    BUSINESS_CERTIFICATE,
    BIRTH_CERTIFICATE;

    val displayName: String
        get() = when (this) {
            AGENT_CARD_REQUEST   -> "Agent Card Request"
            EVD_SIM_CONVERSION   -> "EVD Agent and Sim Conversion Form"
            DYNAMIC_SIM_BULK     -> "Dynamic SIM Cards Bulk Purchase"
            TELECEL_AGENT        -> "Telecel Agent Details"
            AIRTELTIGO_AGENT     -> "AirtelTigo Agent Details"
            BUSINESS_CERTIFICATE -> "Business Certificate Application"
            BIRTH_CERTIFICATE    -> "Birth Certificate Application"
        }

    companion object {
        private val legacyDisplayNames = mapOf(
            EVD_SIM_CONVERSION to setOf(
                "EVD Agent SIM Conversion",
                "EVD Agent SIM Conversion Form"
            )
        )

        fun fromDisplayName(name: String): AgentFormType? =
            entries.find { it.displayName == name }
                ?: entries.find { legacyDisplayNames[it]?.contains(name) == true }
                ?: runCatching { valueOf(name) }.getOrNull()
    }
}

data class StoreOrder(
    val orderId: String,
    val userId: String,
    val agentName: String,
    val itemName: String,
    val quantity: Int,
    val totalAmount: Double,
    val phoneNumber: String,
    val location: String,
    val status: OrderStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class FormSubmission(
    val submissionId: String,
    val userId: String,
    val formType: AgentFormType,
    val applicantName: String = "",
    val phoneNumber: String = "",
    val formData: Map<String, String>,
    /** Document slot name → download URL (after upload) or local path (during submit). */
    val uploadedDocuments: Map<String, String>,
    val status: FormSubmissionStatus,
    val adminNote: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
