package com.momokotle.agent.models

/**
 * Store product from Firestore `storeProducts` (admin-managed).
 */
data class StoreProduct(
    val productId: String,
    val productName: String,
    val productDescription: String,
    val category: StoreCategory,
    val price: Double,
    val stockQuantity: Int,
    val productImageUrl: String?,
    val isAvailable: Boolean,
    val updatedAt: Long = 0L
) {
    val canPurchase: Boolean
        get() = isAvailable && stockQuantity > 0

    val isOutOfStock: Boolean
        get() = stockQuantity <= 0

    /** @deprecated Use [productId]. Kept for order/checkout compatibility. */
    val id: String get() = productId

    val itemName: String get() = productName
    val itemDescription: String get() = productDescription
}

fun StoreCategory.toFirestoreLabel(): String = when (this) {
    StoreCategory.SIM_CARDS -> FIRESTORE_CATEGORY_SIM
    StoreCategory.AGENT_MATERIALS -> FIRESTORE_CATEGORY_MATERIALS
}

fun firestoreCategoryToStoreCategory(label: String): StoreCategory? = when (label.trim()) {
    FIRESTORE_CATEGORY_SIM, "SIM_CARDS" -> StoreCategory.SIM_CARDS
    FIRESTORE_CATEGORY_MATERIALS, "AGENT_MATERIALS" -> StoreCategory.AGENT_MATERIALS
    else -> null
}

const val FIRESTORE_CATEGORY_SIM = "SIM Cards"
const val FIRESTORE_CATEGORY_MATERIALS = "Agent Materials"
