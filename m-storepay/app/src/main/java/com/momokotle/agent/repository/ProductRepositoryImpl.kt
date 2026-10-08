package com.momokotle.agent.repository

import com.momokotle.agent.data.remote.LaravelProductRemoteDataSource
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.database.dao.StoreProductDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.domain.repository.ProductValidationResult
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.toFirestoreLabel
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
class ProductRepositoryImpl @Inject constructor(
    private val storeProductDao: StoreProductDao,
    private val laravelProductRemoteDataSource: LaravelProductRemoteDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ProductRepository {

    private var syncJob: Job? = null

     override fun observeProducts(): Flow<List<StoreProduct>> =
        storeProductDao.observeAll().map { entities ->
            entities.mapNotNull { it.toDomain() }
        }

    override fun observeProductsByCategory(category: StoreCategory): Flow<List<StoreProduct>> =
        storeProductDao.observeByCategory(category.toFirestoreLabel()).map { entities ->
            entities.mapNotNull { it.toDomain() }
        }

    override suspend fun getProduct(productId: String): StoreProduct? =
        storeProductDao.getById(productId)?.toDomain()

    override suspend fun validateForPurchase(
        productId: String,
        quantity: Int
    ): ProductValidationResult {
        val product = getProduct(productId)
            ?: return ProductValidationResult.Invalid("This product is no longer available.")

        if (!product.isAvailable) {
            return ProductValidationResult.Invalid("This product is currently unavailable.")
        }
        if (quantity < 1) {
            return ProductValidationResult.Invalid("Enter a valid quantity.")
        }
        return ProductValidationResult.Valid(product)
    }

    override fun startSync() {
        if (syncJob?.isActive == true) return
        syncJob = applicationScope.launch {
            while (isActive) {
                runCatching {
                    cacheProducts(laravelProductRemoteDataSource.fetchProducts())
                }
                delay(SYNC_INTERVAL_MS)
            }
        }
    }

    override fun stopSync() {
        syncJob?.cancel()
        syncJob = null
    }

    private suspend fun cacheProducts(products: List<StoreProduct>) {
        storeProductDao.deleteAll()
        if (products.isNotEmpty()) {
            storeProductDao.insertAll(products.map { it.toEntity() })
        }
    }

    companion object {
        private const val SYNC_INTERVAL_MS = 60_000L
    }
}
