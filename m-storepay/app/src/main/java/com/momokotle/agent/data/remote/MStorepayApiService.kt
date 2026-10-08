package com.momokotle.agent.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MStorepayApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: AuthLoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRegisterRequest): RegisterPendingResponse

    @POST("api/auth/register/verify")
    suspend fun verifyRegistration(@Body request: VerifyRegistrationOtpRequest): AuthResponse

    @POST("api/auth/register/resend-otp")
    suspend fun resendRegistrationOtp(@Body request: ResendRegistrationOtpRequest): ApiResponse

    @POST("api/auth/logout")
    suspend fun logout(): ApiResponse

    @GET("api/auth/me")
    suspend fun me(): MeResponse

    @GET("api/products")
    suspend fun listProducts(): ProductListResponse

    @GET("api/products/{productId}")
    suspend fun getProduct(@Path("productId") productId: String): ProductResponse

    @GET("api/subscription-plans")
    suspend fun listSubscriptionPlans(): SubscriptionPlanListResponse

    @GET("api/subscription-plans/{planId}")
    suspend fun getSubscriptionPlan(@Path("planId") planId: String): SubscriptionPlanResponse

    @GET("api/settings/business")
    suspend fun getBusinessSettings(): BusinessSettingsResponse

    @GET("api/ussd-flows")
    suspend fun listUssdFlows(): UssdFlowListResponse

    @GET("api/orders")
    suspend fun listOrders(): OrderListResponse

    @GET("api/orders/{orderId}")
    suspend fun getOrder(@Path("orderId") orderId: String): OrderResponse

    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderResponse

    @GET("api/applications/next-id")
    suspend fun reserveApplicationId(): NextApplicationIdResponse

    @GET("api/applications")
    suspend fun listApplications(): ApplicationListResponse

    @GET("api/applications/{applicationId}")
    suspend fun getApplication(@Path("applicationId") applicationId: String): ApplicationResponse

    @POST("api/applications/{applicationId}/submit")
    suspend fun submitApplication(
        @Path("applicationId") applicationId: String,
        @Body request: SubmitApplicationRequest
    ): ApplicationResponse

    @POST("api/applications/{applicationId}/pay")
    suspend fun initializeApplicationPayment(
        @Path("applicationId") applicationId: String
    ): InitializeSubscriptionPaymentResponse

    @POST("api/applications/{applicationId}/pay/manual")
    suspend fun initializeApplicationManualPayment(
        @Path("applicationId") applicationId: String
    ): InitializeManualPaymentResponse

    @Multipart
    @POST("api/applications/{applicationId}/documents")
    suspend fun uploadApplicationDocument(
        @Path("applicationId") applicationId: String,
        @Part("slot_id") slotId: RequestBody,
        @Part file: MultipartBody.Part
    ): ApplicationDocumentResponse

    @DELETE("api/applications/{applicationId}/documents/{documentType}")
    suspend fun deleteApplicationDocument(
        @Path("applicationId") applicationId: String,
        @Path("documentType") documentType: String
    ): ApiResponse

    @GET("api/wallet")
    suspend fun getWallet(): WalletResponse

    @GET("api/wallet/transactions")
    suspend fun listWalletTransactions(): WalletTransactionListResponse

    @GET("api/withdrawals")
    suspend fun listWithdrawals(): WithdrawalListResponse

    @POST("api/withdrawals")
    suspend fun createWithdrawal(@Body request: CreateWithdrawalRequest): WithdrawalResponse

    @GET("api/referrals")
    suspend fun listReferrals(): ReferralListResponse

    @GET("api/referrals/summary")
    suspend fun getReferralSummary(): ReferralSummaryResponse

    @GET("api/referrals/validate/{code}")
    suspend fun validateReferralCode(@Path("code") code: String): ReferralValidateResponse

    @GET("api/notifications")
    suspend fun listNotifications(): NotificationListResponse

    @PATCH("api/notifications/{notificationId}/read")
    suspend fun markNotificationRead(@Path("notificationId") notificationId: String): ApiResponse

    @POST("api/notifications/read-all")
    suspend fun markAllNotificationsRead(): ApiResponse

    @POST("api/subscriptions")
    suspend fun createSubscription(@Body request: CreateSubscriptionRequest): SubscriptionResponse

    @GET("api/subscriptions/access-level")
    suspend fun getAccessLevel(): AccessLevelResponse

    @POST("api/subscriptions/pay")
    suspend fun initializeSubscriptionPayment(
        @Body request: InitializeSubscriptionPaymentRequest
    ): InitializeSubscriptionPaymentResponse

    @POST("api/subscriptions/manual")
    suspend fun initializeManualSubscriptionPayment(
        @Body request: InitializeSubscriptionPaymentRequest
    ): InitializeManualPaymentResponse

    @GET("api/subscriptions/payment/{reference}")
    suspend fun getSubscriptionPayment(
        @Path("reference") reference: String
    ): SubscriptionPaymentStatusResponse
}
