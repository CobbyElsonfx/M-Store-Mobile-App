package com.momokotle.agent.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.firestoreCategoryToStoreCategory

@Entity(tableName = "store_products")
data class StoreProductEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val productDescription: String,
    val category: String,
    val price: Double,
    val stockQuantity: Int,
    val productImageUrl: String?,
    val isAvailable: Boolean,
    val updatedAt: Long
)

fun StoreProductEntity.toDomain(): StoreProduct? {
    val cat = firestoreCategoryToStoreCategory(category) ?: return null
    return StoreProduct(
        productId = productId,
        productName = productName,
        productDescription = productDescription,
        category = cat,
        price = price,
        stockQuantity = stockQuantity,
        productImageUrl = productImageUrl,
        isAvailable = isAvailable,
        updatedAt = updatedAt
    )
}

fun StoreProduct.toEntity() = StoreProductEntity(
    productId = productId,
    productName = productName,
    productDescription = productDescription,
    category = when (category) {
        StoreCategory.SIM_CARDS -> "SIM Cards"
        StoreCategory.AGENT_MATERIALS -> "Agent Materials"
    },
    price = price,
    stockQuantity = stockQuantity,
    productImageUrl = productImageUrl,
    isAvailable = isAvailable,
    updatedAt = updatedAt
)
