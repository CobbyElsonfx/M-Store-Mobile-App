package com.momokotle.agent.models

/**
 * Store order statuses — [firestoreValue] matches admin dashboard (`Pending`, `Processing`, etc.).
 */
enum class OrderStatus(val firestoreValue: String) {
    PENDING("Pending"),
    PAID("Paid"),
    PROCESSING("Processing"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    val generatesCommission: Boolean
        get() = this == DELIVERED || this == COMPLETED

    companion object {
        fun fromFirestore(value: String?): OrderStatus = when (value?.trim()) {
            "Pending", "PENDING", "SUBMITTED" -> PENDING
            "Paid", "PAID" -> PAID
            "Processing", "PROCESSING" -> PROCESSING
            "Delivered", "DELIVERED" -> DELIVERED
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
