package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.StoreOrder
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeMyOrders(userId: String): Flow<List<StoreOrder>>
    suspend fun getOrder(orderId: String): StoreOrder?
    suspend fun placeOrder(
        userId: String,
        agentName: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder>

    fun startSync(userId: String)
    fun stopSync()
}
