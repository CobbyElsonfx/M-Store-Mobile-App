package com.momokotle.agent.repository

import com.momokotle.agent.data.remote.LaravelApiMapper
import com.momokotle.agent.data.remote.LaravelApplicationRemoteDataSource
import com.momokotle.agent.data.form.DocumentSlotRegistry
import com.momokotle.agent.database.dao.FormSubmissionDao
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.di.ApplicationScope
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.utils.BusinessNameValidator
import com.momokotle.agent.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormSubmissionRepositoryImpl @Inject constructor(
    private val formSubmissionDao: FormSubmissionDao,
    private val laravelApplicationRemoteDataSource: LaravelApplicationRemoteDataSource,
    private val networkMonitor: NetworkMonitor,
    @ApplicationScope private val applicationScope: CoroutineScope
) : FormSubmissionRepository {

    private var syncJob: Job? = null
    private var syncedUserId: String? = null

    override suspend fun generateSubmissionId(): String =
        laravelApplicationRemoteDataSource.reserveNextId()

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
        laravelApplicationRemoteDataSource.uploadDocument(
            applicationId = submissionId,
            slotId = slotId,
            localPath = localPath,
            onProgress = onProgress
        )
    }

    override fun observeMySubmissions(userId: String): Flow<List<FormSubmission>> =
        formSubmissionDao.observeByUserId(userId).map { entities ->
            entities.mapNotNull { entity ->
                runCatching { entity.toDomain() }.getOrNull()
            }
        }

    override suspend fun getSubmission(submissionId: String): FormSubmission? =
        runCatching { formSubmissionDao.getById(submissionId)?.toDomain() }.getOrNull()

    override suspend fun refreshSubmission(submissionId: String): FormSubmission? {
        val existing = getSubmission(submissionId) ?: return null
        if (!networkMonitor.isOnline()) {
            return existing
        }
        return runCatching {
            val remote = laravelApplicationRemoteDataSource.fetchMyApplications()
            cacheSubmissionsForUser(existing.userId, remote)
            remote.firstOrNull { it.submissionId == submissionId }
                ?: getSubmission(submissionId)
        }.getOrDefault(existing)
    }

    override suspend fun initializePayment(submissionId: String): Result<SubscriptionCheckout> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to pay"))
        }
        return runCatching { laravelApplicationRemoteDataSource.initializePayment(submissionId) }
    }

    override suspend fun initializeManualPayment(submissionId: String): Result<ManualPaymentCheckout> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to pay"))
        }
        return runCatching { laravelApplicationRemoteDataSource.initializeManualPayment(submissionId) }
    }

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

        BusinessNameValidator.validateFormData(pending.formData)?.let { message ->
            return Result.failure(IllegalArgumentException(message))
        }

        return try {
            val submitted = laravelApplicationRemoteDataSource.submitApplication(
                applicationId = pending.submissionId,
                formType = pending.formType.displayName,
                formData = pending.formData
            )
            formSubmissionDao.insert(submitted.toEntity())
            Result.success(submitted)
        } catch (error: Exception) {
            rollbackUploadedDocuments(pending)
            Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
        }
    }

    override fun startSync(userId: String) {
        if (syncJob?.isActive == true && syncedUserId == userId) return
        stopSync()
        syncedUserId = userId
        syncJob = applicationScope.launch {
            while (isActive) {
                runCatching {
                    cacheSubmissionsForUser(
                        userId,
                        laravelApplicationRemoteDataSource.fetchMyApplications()
                    )
                }
                delay(SYNC_INTERVAL_MS)
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
        submission.uploadedDocuments
            .filter { (_, url) -> url.startsWith("http") }
            .keys
            .forEach { slotId ->
                runCatching {
                    laravelApplicationRemoteDataSource.deleteDocument(
                        applicationId = submission.submissionId,
                        slotId = slotId
                    )
                }
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

    companion object {
        private const val SYNC_INTERVAL_MS = 60_000L
    }
}
