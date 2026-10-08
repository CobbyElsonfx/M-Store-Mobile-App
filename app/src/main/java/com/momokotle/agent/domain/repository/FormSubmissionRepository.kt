package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.FormSubmission
import kotlinx.coroutines.flow.Flow

interface FormSubmissionRepository {
    suspend fun generateSubmissionId(): String

    suspend fun uploadDocumentSlot(
        userId: String,
        submissionId: String,
        slotId: String,
        localPath: String,
        onProgress: (Int) -> Unit = {}
    ): Result<String>

    suspend fun createAndSubmitForm(submission: FormSubmission): Result<FormSubmission>

    fun observeMySubmissions(userId: String): Flow<List<FormSubmission>>

    suspend fun getSubmission(submissionId: String): FormSubmission?

    fun startSync(userId: String)

    fun stopSync()
}
