package com.momokotle.agent.models

enum class StoreCategory {
    SIM_CARDS,
    AGENT_MATERIALS
}

enum class OrderStatus {
    PENDING,
    SUBMITTED,
    COMPLETED,
    CANCELLED
}

enum class FormSubmissionStatus {
    PENDING,
    SUBMITTED,
    APPROVED,
    REJECTED
}

enum class AgentFormType {
    AGENT_CARD_REQUEST,
    EVD_SIM_CONVERSION,
    DYNAMIC_SIM_BULK,
    TELECEL_AGENT,
    AIRTELTIGO_AGENT;

    val displayName: String
        get() = when (this) {
            AGENT_CARD_REQUEST -> "Agent Card Request"
            EVD_SIM_CONVERSION -> "EVD Agent SIM Conversion"
            DYNAMIC_SIM_BULK -> "Dynamic SIM Cards Bulk Purchase"
            TELECEL_AGENT -> "Telecel Agent Details"
            AIRTELTIGO_AGENT -> "AirtelTigo Agent Details"
        }
}

data class StoreItem(
    val id: String,
    val itemName: String,
    val itemDescription: String,
    val price: Double,
    val category: StoreCategory,
    val imageResName: String? = null
)

data class StoreOrder(
    val orderId: String,
    val userId: String,
    val itemName: String,
    val quantity: Int,
    val totalAmount: Double,
    val phoneNumber: String,
    val location: String,
    val status: OrderStatus,
    val createdAt: Long = System.currentTimeMillis()
)

data class FormSubmission(
    val submissionId: String,
    val userId: String,
    val formType: AgentFormType,
    val formData: Map<String, String>,
    val uploadedDocuments: Map<String, String>,
    val status: FormSubmissionStatus,
    val createdAt: Long = System.currentTimeMillis()
)
