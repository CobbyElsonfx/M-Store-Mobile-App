package com.momokotle.agent.repository

import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.repository.UserIdProvider
import com.momokotle.agent.models.StoreOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserIdProviderImpl @Inject constructor(
    private val authRepository: AuthRepository
) : UserIdProvider {
    override suspend fun getUserId(): String =
        authRepository.getCurrentUserId()
            ?: throw IllegalStateException("Not authenticated")
}

@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : StoreRepository {

    override suspend fun createAndSubmitOrder(
        userId: String,
        agentName: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder> = orderRepository.placeOrder(
        userId = userId,
        agentName = agentName,
        itemName = itemName,
        quantity = quantity,
        unitPrice = unitPrice,
        phoneNumber = phoneNumber,
        location = location
    )
}
