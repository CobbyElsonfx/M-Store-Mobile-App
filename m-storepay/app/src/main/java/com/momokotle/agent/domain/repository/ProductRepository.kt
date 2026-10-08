package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import kotlinx.coroutines.flow.Flow

sealed class ProductValidationResult {
    data class Valid(val product: StoreProduct) : ProductValidationResult()
    data class Invalid(val message: String) : ProductValidationResult()
}

interface ProductRepository {
    fun observeProducts(): Flow<List<StoreProduct>>
    fun observeProductsByCategory(category: StoreCategory): Flow<List<StoreProduct>>
    suspend fun getProduct(productId: String): StoreProduct?
    suspend fun validateForPurchase(productId: String, quantity: Int): ProductValidationResult
    fun startSync()
    fun stopSync()
}
