package com.momokotle.agent.repository

import com.momokotle.agent.data.firestore.FirestoreProductDataSource
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val storeProductDao: StoreProductDao,
    private val firestoreProductDataSource: FirestoreProductDataSource,
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
            return ProductValidationResult.Invalid("This product is not available for purchase.")
        }
        if (product.stockQuantity <= 0) {
            return ProductValidationResult.Invalid("This product is out of stock.")
        }
        if (quantity > product.stockQuantity) {
            return ProductValidationResult.Invalid(
                "Only ${product.stockQuantity} item(s) left in stock."
            )
        }
        if (quantity < 1) {
            return ProductValidationResult.Invalid("Enter a valid quantity.")
        }
        return ProductValidationResult.Valid(product)
    }

    override fun startSync() {
        if (syncJob?.isActive == true) return
        syncJob = applicationScope.launch {
            firestoreProductDataSource.observeProductsRealtime().collect { remoteProducts ->
                cacheProducts(remoteProducts)
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
}
