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
    BIRTH_CERTIFICATE,
    PASSPORT;

    val displayName: String
        get() = when (this) {
            AGENT_CARD_REQUEST   -> "Agent Card Request"
            EVD_SIM_CONVERSION   -> "EVD Agent and Sim Conversion Form"
            DYNAMIC_SIM_BULK     -> "Dynamic SIM Cards Bulk Purchase"
            TELECEL_AGENT        -> "Telecel Agent Details"
            AIRTELTIGO_AGENT     -> "AirtelTigo Agent Details"
            BUSINESS_CERTIFICATE -> "Business Certificate Application"
            BIRTH_CERTIFICATE    -> "Birth Certificate Application"
            PASSPORT             -> "Passport Application"
        }

    val serviceTitle: String
        get() = when (this) {
            AGENT_CARD_REQUEST   -> "Agent Card Request"
            EVD_SIM_CONVERSION   -> "EVD Agent SIM Conversion"
            DYNAMIC_SIM_BULK     -> "Dynamic SIM Bulk Purchase"
            TELECEL_AGENT        -> "Telecel Agent Registration"
            AIRTELTIGO_AGENT     -> "AirtelTigo Agent Registration"
            BUSINESS_CERTIFICATE -> "Business Certificate Application"
            BIRTH_CERTIFICATE    -> "Birth Certificate Application"
            PASSPORT             -> "Passport Application"
        }

    val serviceDescription: String
        get() = when (this) {
            AGENT_CARD_REQUEST   -> "Apply for an Agent Card"
            EVD_SIM_CONVERSION   -> "Convert or register an EVD SIM"
            DYNAMIC_SIM_BULK     -> "Request a bulk Dynamic SIM purchase"
            TELECEL_AGENT        -> "Register as a Telecel agent"
            AIRTELTIGO_AGENT     -> "Register as an AirtelTigo agent"
            BUSINESS_CERTIFICATE -> "Apply for a business certificate"
            BIRTH_CERTIFICATE    -> "Apply for a birth certificate"
            PASSPORT             -> "Submit a passport application"
        }

    companion object {
        private val legacyDisplayNames = mapOf(
            EVD_SIM_CONVERSION to setOf(
                "EVD Agent SIM Conversion",
                "EVD Agent SIM Conversion Form"
            ),
            PASSPORT to setOf("Passport Application Form")
        )

        fun fromDisplayName(name: String): AgentFormType? {
            val trimmed = name.trim()
            if (trimmed.isEmpty()) return null
            return entries.find { it.displayName.equals(trimmed, ignoreCase = true) }
                ?: entries.find { it.serviceTitle.equals(trimmed, ignoreCase = true) }
                ?: entries.find { type ->
                    legacyDisplayNames[type]?.any { it.equals(trimmed, ignoreCase = true) } == true
                }
                ?: runCatching { valueOf(trimmed) }.getOrNull()
        }
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
    val requiresPayment: Boolean = false,
    val paymentAmount: Double = 0.0,
    val paymentStatus: String? = null,
    val isPaid: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val paymentLabel: String
        get() = when {
            !requiresPayment -> "Not required"
            isPaid || paymentStatus.equals("Confirmed", ignoreCase = true) -> "Paid"
            else -> "Unpaid"
        }
}
