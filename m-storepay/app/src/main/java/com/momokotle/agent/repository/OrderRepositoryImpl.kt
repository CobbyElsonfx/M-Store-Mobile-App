package com.momokotle.agent.repository

import com.momokotle.agent.data.remote.LaravelApiMapper
import com.momokotle.agent.data.remote.LaravelOrderRemoteDataSource
import com.momokotle.agent.database.dao.StoreOrderDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val storeOrderDao: StoreOrderDao,
    private val laravelOrderRemoteDataSource: LaravelOrderRemoteDataSource,
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
        productId: String,
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

        return try {
            val order = laravelOrderRemoteDataSource.createOrder(
                productId = productId,
                quantity = quantity,
                phoneNumber = phoneNumber,
                location = location
            )
            storeOrderDao.insert(order.toEntity())
            Result.success(order)
        } catch (error: Exception) {
            Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
        }
    }

    override fun startSync(userId: String) {
        if (syncJob?.isActive == true && syncedUserId == userId) return
        stopSync()
        syncedUserId = userId
        syncJob = applicationScope.launch {
            while (isActive) {
                runCatching {
                    cacheOrdersForUser(userId, laravelOrderRemoteDataSource.fetchMyOrders())
                }
                delay(SYNC_INTERVAL_MS)
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

    companion object {
        private const val SYNC_INTERVAL_MS = 60_000L
    }
}
