package com.momokotle.agent.repository

import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.data.remote.FormSubmitRequest
import com.momokotle.agent.data.remote.MStorepayApiService
import com.momokotle.agent.data.remote.OrderSubmitRequest
import com.momokotle.agent.database.dao.FormSubmissionDao
import com.momokotle.agent.database.dao.StoreOrderDao
import com.momokotle.agent.database.entity.toEntity
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.repository.UserIdProvider
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.models.OrderStatus
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.database.entity.toDomain
import com.momokotle.agent.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserIdProviderImpl @Inject constructor(
    private val userAccountDataStore: UserAccountDataStore
) : UserIdProvider {
    override suspend fun getUserId(): String = userAccountDataStore.getUserId()
}

@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val storeOrderDao: StoreOrderDao,
    private val api: MStorepayApiService,
    private val networkMonitor: NetworkMonitor
) : StoreRepository {

    override suspend fun createAndSubmitOrder(
        userId: String,
        itemName: String,
        quantity: Int,
        unitPrice: Double,
        phoneNumber: String,
        location: String
    ): Result<StoreOrder> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to use the store"))
        }
        val order = StoreOrder(
            orderId = UUID.randomUUID().toString(),
            userId = userId,
            itemName = itemName,
            quantity = quantity,
            totalAmount = unitPrice * quantity,
            phoneNumber = phoneNumber,
            location = location,
            status = OrderStatus.PENDING
        )
        storeOrderDao.insert(order.toEntity())
        return runCatching {
            val response = api.submitOrder(
                OrderSubmitRequest(
                    orderId = order.orderId,
                    userId = order.userId,
                    itemName = order.itemName,
                    quantity = order.quantity,
                    totalAmount = order.totalAmount,
                    phoneNumber = order.phoneNumber,
                    location = order.location
                )
            )
            val status = if (response.success) OrderStatus.SUBMITTED else OrderStatus.PENDING
            storeOrderDao.updateStatus(order.orderId, status.name)
            order.copy(status = status)
        }.recoverCatching { error ->
            storeOrderDao.updateStatus(order.orderId, OrderStatus.PENDING.name)
            throw error
        }
    }
}

// TODO (Admin): Submission management (approve/reject) is handled server-side.
// The backend at POST api/v1/forms and its admin dashboard controls status updates.
// This app only reads status via FormSubmission.status from the local DB,
// which is synced when the server responds during submission.
@Singleton
class FormSubmissionRepositoryImpl @Inject constructor(
    private val formSubmissionDao: FormSubmissionDao,
    private val api: MStorepayApiService,
    private val networkMonitor: NetworkMonitor
) : FormSubmissionRepository {

    override fun observeAll(): Flow<List<FormSubmission>> =
        formSubmissionDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun createAndSubmitForm(submission: FormSubmission): Result<FormSubmission> {
        if (!networkMonitor.isOnline()) {
            return Result.failure(IllegalStateException("Internet is required to submit this form"))
        }
        val pending = submission.copy(status = FormSubmissionStatus.PENDING)
        formSubmissionDao.insert(pending.toEntity())
        return runCatching {
            val metaResponse = api.submitFormMetadata(
                FormSubmitRequest(
                    submissionId = pending.submissionId,
                    userId = pending.userId,
                    formType = pending.formType.name,
                    formData = pending.formData
                )
            )
            pending.uploadedDocuments.forEach { (slotId, path) ->
                val file = File(path)
                if (file.exists()) {
                    val body = file.asRequestBody(
                        guessMediaType(file.name).toMediaTypeOrNull()
                    )
                    val part = MultipartBody.Part.createFormData("file", file.name, body)
                    api.uploadFormDocument(
                        submissionId = pending.submissionId,
                        slotId = slotId.toRequestBody("text/plain".toMediaTypeOrNull()),
                        file = part
                    )
                }
            }
            val status = if (metaResponse.success) {
                FormSubmissionStatus.SUBMITTED
            } else {
                FormSubmissionStatus.PENDING
            }
            formSubmissionDao.updateStatus(pending.submissionId, status.name)
            pending.copy(status = status)
        }.onFailure {
            formSubmissionDao.updateStatus(pending.submissionId, FormSubmissionStatus.PENDING.name)
        }
    }

    private fun guessMediaType(fileName: String): String = when {
        fileName.endsWith(".pdf", true) -> "application/pdf"
        fileName.endsWith(".png", true) -> "image/png"
        fileName.endsWith(".jpg", true) || fileName.endsWith(".jpeg", true) -> "image/jpeg"
        else -> "application/octet-stream"
    }
}
