package com.momokotle.agent.repository

import com.momokotle.agent.data.firestore.FirebaseSubmissionStorage
import com.momokotle.agent.data.firestore.FirestoreFormSubmissionDataSource
import com.momokotle.agent.data.firestore.SubmissionIdGenerator
import com.momokotle.agent.database.dao.FormSubmissionDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormSubmissionRepositoryImpl @Inject constructor(
    private val formSubmissionDao: FormSubmissionDao,
    private val firestoreDataSource: FirestoreFormSubmissionDataSource,
    private val submissionStorage: FirebaseSubmissionStorage,
    private val submissionIdGenerator: SubmissionIdGenerator,
    private val networkMonitor: NetworkMonitor,
    @ApplicationScope private val applicationScope: CoroutineScope
) : FormSubmissionRepository {

    private var syncJob: Job? = null
    private var syncedUserId: String? = null

    override suspend fun generateSubmissionId(): String = submissionIdGenerator.generateNextId()

    override suspend fun uploadDocumentSlot(
        userId: String,
        submissionId: String,
        slotId: String,
        localPath: String,
        onProgress: (Int) -> Unit
    ): Result<String> = runCatching {
        if (!networkMonitor.isOnline()) {
            error("Internet is required to upload documents")
        }
        submissionStorage.uploadDocument(
            userId = userId,
            submissionId = submissionId,
            slotId = slotId,
            localPath = localPath,
            onProgress = onProgress
        )
    }

    override fun observeMySubmissions(userId: String): Flow<List<FormSubmission>> =
        formSubmissionDao.observeByUserId(userId).map { entities ->
            entities.mapNotNull { it.toDomain() }
        }

    override suspend fun getSubmission(submissionId: String): FormSubmission? =
        formSubmissionDao.getById(submissionId)?.toDomain()

    override suspend fun createAndSubmitForm(submission: FormSubmission): Result<FormSubmission> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to submit this form"))
        }

        val applicantName = resolveApplicantName(submission.formData)
        val phone = submission.formData["phoneNumber"]
            ?: submission.formData["whatsappNumber"]
            ?: ""

        val pending = submission.copy(
            applicantName = applicantName,
            phoneNumber = phone,
            status = FormSubmissionStatus.PENDING
        )

        val localPaths = pending.uploadedDocuments.filterValues { !it.startsWith("http") }
        if (localPaths.isNotEmpty()) {
            return Result.failure(
                IllegalStateException("Some documents are still uploading. Please wait for uploads to finish.")
            )
        }
        if (pending.uploadedDocuments.isEmpty()) {
            return Result.failure(
                IllegalStateException("No documents to upload. Please attach the required files.")
            )
        }

        return runCatching {
            firestoreDataSource.createSubmission(pending)
            formSubmissionDao.insert(pending.toEntity())
            pending
        }.onFailure {
            rollbackUploadedDocuments(pending)
        }
    }

    override fun startSync(userId: String) {
        if (syncJob?.isActive == true && syncedUserId == userId) return
        stopSync()
        syncedUserId = userId
        syncJob = applicationScope.launch {
            firestoreDataSource.observeSubmissionsForUser(userId).collect { remote ->
                cacheSubmissionsForUser(userId, remote)
            }
        }
    }

    override fun stopSync() {
        syncJob?.cancel()
        syncJob = null
        syncedUserId = null
    }

    private suspend fun cacheSubmissionsForUser(userId: String, submissions: List<FormSubmission>) {
        formSubmissionDao.deleteByUserId(userId)
        if (submissions.isNotEmpty()) {
            formSubmissionDao.insertAll(submissions.map { it.toEntity() })
        }
    }

    private suspend fun rollbackUploadedDocuments(submission: FormSubmission) {
        submission.uploadedDocuments.values.filter { it.startsWith("http") }.forEach { url ->
            runCatching { submissionStorage.deleteByUrl(url) }
        }
    }

    private fun resolveApplicantName(formData: Map<String, String>): String {
        return formData["fullName"]?.takeIf { it.isNotBlank() }
            ?: formData["firstName"]?.let { first ->
                val surname = formData["surname"].orEmpty()
                "$first $surname".trim().takeIf { it.isNotBlank() }
            }
            ?: formData["businessName"]
            ?: ""
    }
}
