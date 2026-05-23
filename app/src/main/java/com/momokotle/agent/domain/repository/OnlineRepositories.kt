package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.StoreOrder

interface StoreRepository {
    suspend fun createAndSubmitOrder(
        userId: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder>
}

interface FormSubmissionRepository {
    suspend fun createAndSubmitForm(submission: FormSubmission): Result<FormSubmission>
}

interface UserIdProvider {
    suspend fun getUserId(): String
}
