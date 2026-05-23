package com.momokotle.agent.data.remote

data class ApiResponse(
    val success: Boolean,
    val message: String? = null
)

data class OrderSubmitRequest(
    val orderId: String,
    val userId: String,
    val itemName: String,
    val quantity: Int,
    val totalAmount: Double,
    val phoneNumber: String,
    val location: String
)

data class FormSubmitRequest(
    val submissionId: String,
    val userId: String,
    val formType: String,
    val formData: Map<String, String>
)
