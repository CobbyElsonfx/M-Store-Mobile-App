package com.momokotle.agent.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.firestoreCategoryToStoreCategory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreProductDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun observeProductsRealtime(): Flow<List<StoreProduct>> = callbackFlow {
        val registration: ListenerRegistration = firestore
            .collection(COLLECTION_STORE_PRODUCTS)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val products = snapshot.documents.mapNotNull { it.toStoreProduct() }
                trySend(products)
            }
        awaitClose { registration.remove() }
    }

    companion object {
        const val COLLECTION_STORE_PRODUCTS = "storeProducts"

        const val FIELD_PRODUCT_NAME = "productName"
        const val FIELD_PRODUCT_DESCRIPTION = "productDescription"
        const val FIELD_CATEGORY = "category"
        const val FIELD_PRICE = "price"
        const val FIELD_STOCK_QUANTITY = "stockQuantity"
        const val FIELD_PRODUCT_IMAGE_URL = "productImageUrl"
        const val FIELD_IS_AVAILABLE = "isAvailable"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.toStoreProduct(): StoreProduct? {
    val categoryLabel = getString(FirestoreProductDataSource.FIELD_CATEGORY) ?: return null
    val category = firestoreCategoryToStoreCategory(categoryLabel) ?: return null
    val name = getString(FirestoreProductDataSource.FIELD_PRODUCT_NAME) ?: return null

    return StoreProduct(
        productId = id,
        productName = name,
        productDescription = getString(FirestoreProductDataSource.FIELD_PRODUCT_DESCRIPTION).orEmpty(),
        category = category,
        price = getDouble(FirestoreProductDataSource.FIELD_PRICE) ?: 0.0,
        stockQuantity = getLong(FirestoreProductDataSource.FIELD_STOCK_QUANTITY)?.toInt()
            ?: getDouble(FirestoreProductDataSource.FIELD_STOCK_QUANTITY)?.toInt()
            ?: 0,
        productImageUrl = getString(FirestoreProductDataSource.FIELD_PRODUCT_IMAGE_URL)
            ?.takeIf { it.isNotBlank() },
        isAvailable = getBoolean(FirestoreProductDataSource.FIELD_IS_AVAILABLE) ?: true,
        updatedAt = timestampMillis(getTimestamp(FirestoreProductDataSource.FIELD_UPDATED_AT))
            ?: timestampMillis(getTimestamp(FirestoreProductDataSource.FIELD_CREATED_AT))
            ?: 0L
    )
}

private fun timestampMillis(timestamp: Timestamp?): Long? =
    timestamp?.toDate()?.time
