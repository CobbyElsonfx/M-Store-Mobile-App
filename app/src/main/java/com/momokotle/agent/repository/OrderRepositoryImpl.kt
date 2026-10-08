package com.momokotle.agent.repository

import com.momokotle.agent.data.firestore.FirestoreOrderDataSource
import com.momokotle.agent.database.dao.StoreOrderDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.models.OrderStatus
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val storeOrderDao: StoreOrderDao,
    private val firestoreOrderDataSource: FirestoreOrderDataSource,
    private val networkMonitor: NetworkMonitor,
    @ApplicationScope private val applicationScope: CoroutineScope
) : OrderRepository {

    private var syncJob: Job? = null
    private var syncedUserId: String? = null

    override fun observeMyOrders(userId: String): Flow<List<StoreOrder>> =
        storeOrderDao.observeByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getOrder(orderId: String): StoreOrder? =
        storeOrderDao.getById(orderId)?.toDomain()

    override suspend fun placeOrder(
        userId: String,
        agentName: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to place an order"))
        }

        val now = System.currentTimeMillis()
        val order = StoreOrder(
            orderId = UUID.randomUUID().toString(),
            userId = userId,
            agentName = agentName,
            itemName = itemName,
            quantity = quantity,
            totalAmount = unitPrice * quantity,
            phoneNumber = phoneNumber,
            location = location,
            status = OrderStatus.PENDING,
            createdAt = now,
            updatedAt = now
        )

        storeOrderDao.insert(order.toEntity())

        return runCatching {
            firestoreOrderDataSource.createOrder(order)
            val synced = order.copy(updatedAt = System.currentTimeMillis())
            storeOrderDao.insert(synced.toEntity())
            synced
        }.onFailure {
            storeOrderDao.insert(
                order.copy(status = OrderStatus.PENDING).toEntity()
            )
        }
    }

    override fun startSync(userId: String) {
        if (syncJob?.isActive == true && syncedUserId == userId) return
        stopSync()
        syncedUserId = userId
        syncJob = applicationScope.launch {
            firestoreOrderDataSource.observeOrdersForUser(userId).collect { remoteOrders ->
                cacheOrdersForUser(userId, remoteOrders)
            }
        }
    }

    override fun stopSync() {
        syncJob?.cancel()
        syncJob = null
        syncedUserId = null
    }

    private suspend fun cacheOrdersForUser(userId: String, orders: List<StoreOrder>) {
        storeOrderDao.deleteByUserId(userId)
        if (orders.isNotEmpty()) {
            storeOrderDao.insertAll(orders.map { it.toEntity() })
        }
    }
}
