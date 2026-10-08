package com.momokotle.agent.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.momokotle.agent.models.OrderStatus
import com.momokotle.agent.models.StoreOrder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreOrderDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createOrder(order: StoreOrder) {
        val data = hashMapOf(
            FIELD_ORDER_ID to order.orderId,
            FIELD_USER_ID to order.userId,
            FIELD_AGENT_NAME to order.agentName,
            FIELD_PHONE_NUMBER to order.phoneNumber,
            FIELD_LOCATION to order.location,
            FIELD_ITEM_NAME to order.itemName,
            FIELD_QUANTITY to order.quantity,
            FIELD_TOTAL_AMOUNT to order.totalAmount,
            FIELD_STATUS to order.status.firestoreValue,
            FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            FIELD_UPDATED_AT to FieldValue.serverTimestamp()
        )
        firestore.collection(COLLECTION_STORE_ORDERS)
            .document(order.orderId)
            .set(data)
            .await()
    }

    fun observeOrdersForUser(userId: String): Flow<List<StoreOrder>> = callbackFlow {
        val registration: ListenerRegistration = firestore
            .collection(COLLECTION_STORE_ORDERS)
            .whereEqualTo(FIELD_USER_ID, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val orders = snapshot.documents.mapNotNull { it.toStoreOrder() }
                    .sortedByDescending { it.createdAt }
                trySend(orders)
            }
        awaitClose { registration.remove() }
    }

    companion object {
        const val COLLECTION_STORE_ORDERS = "storeOrders"

        const val FIELD_ORDER_ID = "orderId"
        const val FIELD_USER_ID = "userId"
        const val FIELD_AGENT_NAME = "agentName"
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_LOCATION = "location"
        const val FIELD_ITEM_NAME = "itemName"
        const val FIELD_QUANTITY = "quantity"
        const val FIELD_TOTAL_AMOUNT = "totalAmount"
        const val FIELD_STATUS = "status"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.toStoreOrder(): StoreOrder? {
    val orderId = getString(FirestoreOrderDataSource.FIELD_ORDER_ID) ?: id
    val userId = getString(FirestoreOrderDataSource.FIELD_USER_ID) ?: return null
    val itemName = getString(FirestoreOrderDataSource.FIELD_ITEM_NAME) ?: return null

    return StoreOrder(
        orderId = orderId,
        userId = userId,
        agentName = getString(FirestoreOrderDataSource.FIELD_AGENT_NAME).orEmpty(),
        itemName = itemName,
        quantity = getLong(FirestoreOrderDataSource.FIELD_QUANTITY)?.toInt()
            ?: getDouble(FirestoreOrderDataSource.FIELD_QUANTITY)?.toInt()
            ?: 0,
        totalAmount = getDouble(FirestoreOrderDataSource.FIELD_TOTAL_AMOUNT) ?: 0.0,
        phoneNumber = getString(FirestoreOrderDataSource.FIELD_PHONE_NUMBER).orEmpty(),
        location = getString(FirestoreOrderDataSource.FIELD_LOCATION).orEmpty(),
        status = OrderStatus.fromFirestore(getString(FirestoreOrderDataSource.FIELD_STATUS)),
        createdAt = timestampMillis(getTimestamp(FirestoreOrderDataSource.FIELD_CREATED_AT))
            ?: 0L,
        updatedAt = timestampMillis(getTimestamp(FirestoreOrderDataSource.FIELD_UPDATED_AT))
            ?: timestampMillis(getTimestamp(FirestoreOrderDataSource.FIELD_CREATED_AT))
            ?: 0L
    )
}

private fun timestampMillis(timestamp: Timestamp?): Long? =
    timestamp?.toDate()?.time
