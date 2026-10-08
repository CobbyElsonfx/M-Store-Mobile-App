package com.momokotle.agent.data.remote

import com.momokotle.agent.models.AppNotification
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.UserSubscription
import com.momokotle.agent.models.ReferralInfo
import com.momokotle.agent.models.ReferralRecord
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WithdrawalRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaravelProductRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchProducts(): List<StoreProduct> =
        api.listProducts()
            .data
            .mapNotNull(LaravelApiMapper::mapProduct)
}

@Singleton
class LaravelBusinessSettingsRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchSettings(): BusinessSettings =
        LaravelApiMapper.mapBusinessSettings(api.getBusinessSettings().data)
}

@Singleton
class LaravelSubscriptionPlanRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchActivePlans(): List<SubscriptionPlan> =
        api.listSubscriptionPlans()
            .data
            .map(LaravelApiMapper::mapSubscriptionPlan)

    suspend fun fetchPlan(planId: String): SubscriptionPlan? =
        runCatching {
            LaravelApiMapper.mapSubscriptionPlan(api.getSubscriptionPlan(planId).data)
        }.getOrNull()
}

@Singleton
class LaravelOrderRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchMyOrders(): List<StoreOrder> =
        api.listOrders()
            .data
            .map(LaravelApiMapper::mapOrder)

    suspend fun createOrder(
        productId: String,
        quantity: Int,
        phoneNumber: String,
        location: String
    ): StoreOrder =
        LaravelApiMapper.mapOrder(
            api.createOrder(
                CreateOrderRequest(
                    productId = productId,
                    quantity = quantity,
                    phoneNumber = phoneNumber,
                    location = location
                )
            ).data
        )
}

@Singleton
class LaravelApplicationRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun reserveNextId(): String =
        api.reserveApplicationId().data.id

    suspend fun fetchMyApplications(): List<FormSubmission> =
        api.listApplications()
            .data
            .mapNotNull { dto ->
                runCatching { LaravelApiMapper.mapApplication(dto) }.getOrNull()
            }

    suspend fun submitApplication(
        applicationId: String,
        formType: String,
        formData: Map<String, String>
    ): FormSubmission =
        LaravelApiMapper.mapApplication(
            api.submitApplication(
                applicationId = applicationId,
                request = SubmitApplicationRequest(
                    formType = formType,
                    formData = formData
                )
            ).data
        ) ?: throw IllegalStateException("Unsupported application type returned by server.")

    suspend fun initializePayment(applicationId: String): SubscriptionCheckout =
        LaravelApiMapper.mapSubscriptionCheckout(api.initializeApplicationPayment(applicationId))

    suspend fun initializeManualPayment(applicationId: String): ManualPaymentCheckout =
        LaravelApiMapper.mapManualPaymentCheckout(api.initializeApplicationManualPayment(applicationId))

    suspend fun uploadDocument(
        applicationId: String,
        slotId: String,
        localPath: String,
        onProgress: (Int) -> Unit = {}
    ): String {
        val file = File(localPath)
        if (!file.exists()) {
            throw IllegalArgumentException(
                "Uploaded file is missing on device ($slotId). Please select the file again."
            )
        }
        if (file.length() == 0L) {
            throw IllegalArgumentException(
                "Uploaded file is empty ($slotId). Please select the file again."
            )
        }

        onProgress(0)
        val mediaType = guessMediaType(file).toMediaType()
        val fileBody = file.asRequestBody(mediaType)
        val multipart = MultipartBody.Part.createFormData("file", file.name, fileBody)
        val slotBody = slotId.toRequestBody("text/plain".toMediaType())

        val url = api.uploadApplicationDocument(
            applicationId = applicationId,
            slotId = slotBody,
            file = multipart
        ).data.url
        onProgress(100)
        return url
    }

    suspend fun deleteDocument(applicationId: String, slotId: String) {
        val documentType = com.momokotle.agent.data.form.DocumentSlotRegistry
            .mappingFor(slotId)
            .firestoreUrlKey
        api.deleteApplicationDocument(applicationId, documentType)
    }

    private fun guessMediaType(file: File): String =
        when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "pdf" -> "application/pdf"
            else -> "application/octet-stream"
        }
}

@Singleton
class LaravelWalletRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchWallet(): Wallet =
        LaravelApiMapper.mapWallet(api.getWallet().data)

    suspend fun fetchTransactions(): List<WalletTransactionRecord> =
        api.listWalletTransactions()
            .data
            .map(LaravelApiMapper::mapWalletTransaction)

    suspend fun fetchWithdrawals(): List<WithdrawalRequest> =
        api.listWithdrawals()
            .data
            .map(LaravelApiMapper::mapWithdrawal)

    suspend fun requestWithdrawal(
        amount: Double,
        paymentMethod: PaymentProvider,
        paymentNumber: String
    ): WithdrawalRequest =
        LaravelApiMapper.mapWithdrawal(
            api.createWithdrawal(
                CreateWithdrawalRequest(
                    amount = amount,
                    paymentMethod = paymentMethod.firestoreValue,
                    paymentNumber = paymentNumber
                )
            ).data
        )
}

@Singleton
class LaravelReferralRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchSummary(): ReferralInfo =
        LaravelApiMapper.mapReferralSummary(api.getReferralSummary().data)

    suspend fun fetchReferrals(): List<ReferralRecord> =
        api.listReferrals()
            .data
            .map(LaravelApiMapper::mapReferral)

    suspend fun validateReferralCode(code: String): String =
        api.validateReferralCode(code).data.referrerUserId
}

@Singleton
class LaravelNotificationRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun fetchNotifications(): List<AppNotification> =
        api.listNotifications()
            .data
            .map(LaravelApiMapper::mapNotification)

    suspend fun markAsRead(notificationId: String) {
        api.markNotificationRead(notificationId)
    }

    suspend fun markAllAsRead() {
        api.markAllNotificationsRead()
    }
}

@Singleton
class LaravelSubscriptionRemoteDataSource @Inject constructor(
    private val api: MStorepayApiService
) {
    suspend fun purchaseSubscription(
        planId: String,
        provider: PaymentProvider,
        paymentReference: String
    ): UserSubscription =
        LaravelApiMapper.mapSubscription(
            api.createSubscription(
                CreateSubscriptionRequest(
                    planId = planId,
                    paymentProvider = provider.firestoreValue,
                    paymentReference = paymentReference.trim()
                )
            ).data
        )

    suspend fun initializePayment(planId: String): SubscriptionCheckout {
        val response = api.initializeSubscriptionPayment(
            InitializeSubscriptionPaymentRequest(subscriptionPlanId = planId)
        )
        return LaravelApiMapper.mapSubscriptionCheckout(response)
    }

    suspend fun initializeManualPayment(planId: String): ManualPaymentCheckout {
        val response = api.initializeManualSubscriptionPayment(
            InitializeSubscriptionPaymentRequest(subscriptionPlanId = planId)
        )
        return LaravelApiMapper.mapManualPaymentCheckout(response)
    }

    suspend fun fetchPaymentStatus(reference: String): SubscriptionPaymentStatus =
        LaravelApiMapper.mapSubscriptionPaymentStatus(api.getSubscriptionPayment(reference).data)

    suspend fun fetchAccessLevel(): AccessLevel =
        LaravelApiMapper.mapAccessLevel(api.getAccessLevel().data.accessLevel)
}
