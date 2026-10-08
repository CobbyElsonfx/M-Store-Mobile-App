package com.momokotle.agent.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.momokotle.agent.data.form.DocumentSlotRegistry
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreFormSubmissionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createSubmission(submission: FormSubmission) {
        val documents = DocumentSlotRegistry.slotUrlsToFirestoreDocuments(submission.uploadedDocuments)
        val data = hashMapOf(
            FIELD_SUBMISSION_ID to submission.submissionId,
            FIELD_USER_ID to submission.userId,
            FIELD_FORM_TYPE to submission.formType.displayName,
            FIELD_APPLICANT_NAME to submission.applicantName,
            FIELD_PHONE_NUMBER to submission.phoneNumber,
            FIELD_FORM_DATA to submission.formData,
            FIELD_DOCUMENTS to documents,
            FIELD_STATUS to submission.status.firestoreValue,
            FIELD_ADMIN_NOTE to "",
            FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            FIELD_UPDATED_AT to FieldValue.serverTimestamp()
        )
        firestore.collection(COLLECTION_FORM_SUBMISSIONS)
            .document(submission.submissionId)
            .set(data)
            .await()
    }

    suspend fun updateSubmissionStatus(
        submissionId: String,
        status: FormSubmissionStatus,
        adminNote: String?
    ) {
        firestore.collection(COLLECTION_FORM_SUBMISSIONS)
            .document(submissionId)
            .update(
                mapOf(
                    FIELD_STATUS to status.firestoreValue,
                    FIELD_ADMIN_NOTE to (adminNote.orEmpty()),
                    FIELD_UPDATED_AT to FieldValue.serverTimestamp()
                )
            )
            .await()
    }

    fun observeSubmissionsForUser(userId: String): Flow<List<FormSubmission>> = callbackFlow {
        val merged = mutableMapOf<String, FormSubmission>()
        val registrations = listOf(COLLECTION_FORM_SUBMISSIONS, LEGACY_COLLECTION).map { collectionName ->
            firestore.collection(collectionName)
                .whereEqualTo(FIELD_USER_ID, userId)
                .addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null) {
                        snapshot.documents.forEach { doc ->
                            doc.toFormSubmission()?.let { merged[it.submissionId] = it }
                        }
                    }
                    trySend(merged.values.sortedByDescending { it.createdAt })
                }
        }
        awaitClose { registrations.forEach { it.remove() } }
    }

    companion object {
        const val COLLECTION_FORM_SUBMISSIONS = "form_submissions"
        const val LEGACY_COLLECTION = "formSubmissions"

        const val FIELD_SUBMISSION_ID = "submissionId"
        const val FIELD_USER_ID = "userId"
        const val FIELD_FORM_TYPE = "formType"
        const val FIELD_APPLICANT_NAME = "applicantName"
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_FORM_DATA = "formData"
        const val FIELD_DOCUMENTS = "documents"
        const val FIELD_UPLOADED_DOCUMENTS = "uploadedDocuments"
        const val FIELD_STATUS = "status"
        const val FIELD_ADMIN_NOTE = "adminNote"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

@Suppress("UNCHECKED_CAST")
private fun com.google.firebase.firestore.DocumentSnapshot.toFormSubmission(): FormSubmission? {
    val submissionId = getString(FirestoreFormSubmissionDataSource.FIELD_SUBMISSION_ID) ?: id
    val userId = getString(FirestoreFormSubmissionDataSource.FIELD_USER_ID) ?: return null
    val formTypeLabel = getString(FirestoreFormSubmissionDataSource.FIELD_FORM_TYPE) ?: return null
    val formType = AgentFormType.fromDisplayName(formTypeLabel) ?: return null

    val formData = get(FirestoreFormSubmissionDataSource.FIELD_FORM_DATA) as? Map<String, String>
        ?: emptyMap()

    val documentsField = get(FirestoreFormSubmissionDataSource.FIELD_DOCUMENTS) as? Map<String, String>
    val legacyDocuments = get(FirestoreFormSubmissionDataSource.FIELD_UPLOADED_DOCUMENTS) as? Map<String, String>
    val uploadedDocuments = when {
        documentsField != null -> DocumentSlotRegistry.firestoreDocumentsToSlotUrls(documentsField)
        legacyDocuments != null -> legacyDocuments
        else -> emptyMap()
    }

    val adminNote = getString(FirestoreFormSubmissionDataSource.FIELD_ADMIN_NOTE)
        ?.takeIf { it.isNotBlank() }

    return FormSubmission(
        submissionId = submissionId,
        userId = userId,
        formType = formType,
        applicantName = getString(FirestoreFormSubmissionDataSource.FIELD_APPLICANT_NAME).orEmpty(),
        phoneNumber = getString(FirestoreFormSubmissionDataSource.FIELD_PHONE_NUMBER).orEmpty(),
        formData = formData,
        uploadedDocuments = uploadedDocuments,
        status = FormSubmissionStatus.fromFirestore(
            getString(FirestoreFormSubmissionDataSource.FIELD_STATUS)
        ),
        adminNote = adminNote,
        createdAt = timestampMillis(getTimestamp(FirestoreFormSubmissionDataSource.FIELD_CREATED_AT))
            ?: 0L,
        updatedAt = timestampMillis(getTimestamp(FirestoreFormSubmissionDataSource.FIELD_UPDATED_AT))
            ?: timestampMillis(getTimestamp(FirestoreFormSubmissionDataSource.FIELD_CREATED_AT))
            ?: 0L
    )
}

private fun timestampMillis(timestamp: Timestamp?): Long? =
    timestamp?.toDate()?.time
