package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.StoreOrder

interface StoreRepository {
    suspend fun createAndSubmitOrder(
        userId: String,
        productId: String,
        agentName: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder>
}

interface UserIdProvider {
    suspend fun getUserId(): String
}
