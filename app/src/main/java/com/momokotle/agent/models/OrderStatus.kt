package com.momokotle.agent.models

/**
 * Store order statuses — [firestoreValue] matches admin dashboard (`Pending`, `Processing`, etc.).
 */
enum class OrderStatus(val firestoreValue: String) {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    companion object {
        fun fromFirestore(value: String?): OrderStatus = when (value?.trim()) {
            "Pending", "PENDING", "SUBMITTED" -> PENDING
            "Processing", "PROCESSING" -> PROCESSING
            "Completed", "COMPLETED" -> COMPLETED
            "Cancelled", "CANCELLED" -> CANCELLED
            else -> PENDING
        }
    }
}

object OrderStatusMapper {
    /** Persisted in Room as Firestore display string. */
    fun toStorageValue(status: OrderStatus): String = status.firestoreValue

    fun fromStorage(value: String): OrderStatus = OrderStatus.fromFirestore(value)
}
