package com.momokotle.agent.models

enum class FormSubmissionStatus(val firestoreValue: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    RESUBMISSION_REQUIRED("ResubmissionRequired");

    companion object {
        fun fromFirestore(value: String?): FormSubmissionStatus = when (value?.trim()) {
            "Pending", "PENDING" -> PENDING
            "Submitted", "SUBMITTED" -> PENDING
            "Approved", "APPROVED" -> APPROVED
            "Rejected", "REJECTED" -> REJECTED
            "ResubmissionRequired", "RESUBMISSION_REQUIRED" -> RESUBMISSION_REQUIRED
            else -> PENDING
        }
    }
}

object FormSubmissionStatusMapper {
    fun toStorageValue(status: FormSubmissionStatus): String = status.firestoreValue
    fun fromStorage(value: String): FormSubmissionStatus = FormSubmissionStatus.fromFirestore(value)
}
