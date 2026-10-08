package com.momokotle.agent.data.remote

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ApiErrorBody(
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)

data class AuthLoginRequest(
    val email: String,
    val password: String
)

data class AuthRegisterRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val location: String,
    val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String,
    @SerializedName("referral_code") val referralCode: String? = null
)

data class AuthResponse(
    val message: String? = null,
    val token: String? = null,
    @SerializedName("token_type") val tokenType: String? = null,
    val user: ApiUserDto? = null
)

data class RegisterPendingResponse(
    val message: String? = null,
    @SerializedName("requires_verification") val requiresVerification: Boolean = false,
    val email: String? = null
)

data class VerifyRegistrationOtpRequest(
    val email: String,
    @SerializedName("otp_code") val otpCode: String
)

data class ResendRegistrationOtpRequest(
    val email: String
)

data class MeResponse(
    val user: ApiUserDto
)

data class UssdFlowListResponse(
    val data: List<ApiUssdFlowDto>
)

data class ApiUssdFlowDto(
    @SerializedName("flowKey") val flowKey: String,
    val name: String,
    @SerializedName("ussdCode") val ussdCode: String,
    val steps: List<ApiUssdStepDto> = emptyList(),
    @SerializedName("isActive") val isActive: Boolean = true,
    @SerializedName("sortOrder") val sortOrder: Int = 0
)

data class ApiUssdStepDto(
    val type: String,
    val value: String? = null,
    val hints: List<String>? = null
)

data class ApiUserDto(
    val id: Long,
    val email: String,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    val location: String? = null,
    @SerializedName("account_status") val accountStatus: String? = null,
    @SerializedName("referral_code") val referralCode: String? = null,
    @SerializedName("subscription_status") val subscriptionStatus: String? = null,
    @SerializedName("trial_end_date") val trialEndDate: String? = null,
    @SerializedName("subscription_expiry_date") val subscriptionExpiryDate: String? = null,
    @SerializedName("access_level") val accessLevel: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
)

data class ApiProductDto(
    val id: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("productDescription") val productDescription: String? = null,
    val category: String,
    val price: Double,
    @SerializedName("stockQuantity") val stockQuantity: Int = 0,
    @SerializedName("productImageUrl") val productImageUrl: String? = null,
    @SerializedName("isAvailable") val isAvailable: Boolean = true,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class ProductListResponse(
    val data: List<ApiProductDto>
)

data class ProductResponse(
    val data: ApiProductDto
)

data class ApiSubscriptionPlanDto(
    val id: String,
    @SerializedName("planName") val planName: String,
    val price: Double,
    @SerializedName("durationDays") val durationDays: Int,
    val status: String,
    @SerializedName("sortOrder") val sortOrder: Int = 0
)

data class SubscriptionPlanListResponse(
    val data: List<ApiSubscriptionPlanDto>
)

data class SubscriptionPlanResponse(
    val data: ApiSubscriptionPlanDto
)

data class ApiBusinessSettingsDto(
    @SerializedName("minimumWithdrawal") val minimumWithdrawal: Double = 10.0,
    @SerializedName("largeTransactionWarningAmount") val largeTransactionWarningAmount: Double = 1_000.0,
    @SerializedName("trialDays") val trialDays: Int = 7,
    val currency: String = "GHS",
    @SerializedName("taxRate") val taxRate: Double = 0.0,
    @SerializedName("storeEnabled") val storeEnabled: Boolean = true,
    @SerializedName("subscriptionEnabled") val subscriptionEnabled: Boolean = true,
    @SerializedName("referralShareLinkBase") val referralShareLinkBase: String = "https://momokotle.app/ref/",
    @SerializedName("supportWhatsAppNumber") val supportWhatsAppNumber: String? = null,
    @SerializedName("paystackPaymentEnabled") val paystackPaymentEnabled: Boolean = true,
    @SerializedName("manualPaymentEnabled") val manualPaymentEnabled: Boolean = true,
    @SerializedName("manualPayment") val manualPayment: ApiManualPaymentDetailsDto? = null
)

data class ApiManualPaymentDetailsDto(
    val accountName: String? = null,
    val accountNumber: String? = null,
    val bankName: String? = null,
    val momoNetwork: String? = null,
    val momoNumber: String? = null,
    val instructions: String? = null,
    val supportContact: String? = null
)

data class BusinessSettingsResponse(
    val data: ApiBusinessSettingsDto
)

data class CreateOrderRequest(
    @SerializedName("product_id") val productId: String,
    val quantity: Int,
    @SerializedName("phone_number") val phoneNumber: String,
    val location: String
)

data class ApiOrderDto(
    val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("productId") val productId: String? = null,
    @SerializedName("agentName") val agentName: String,
    @SerializedName("itemName") val itemName: String,
    val quantity: Int,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("phoneNumber") val phoneNumber: String,
    val location: String,
    val status: String,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class OrderListResponse(
    val data: List<ApiOrderDto>
)

data class OrderResponse(
    val data: ApiOrderDto
)

data class NextApplicationIdResponse(
    val data: NextApplicationIdData
)

data class NextApplicationIdData(
    val id: String
)

data class SubmitApplicationRequest(
    @SerializedName("form_type") val formType: String,
    @SerializedName("form_data") val formData: Map<String, String>
)

data class ApiApplicationDto(
    val id: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("formType") val formType: String? = null,
    @SerializedName("applicantName") val applicantName: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("formData") val formData: JsonElement? = null,
    val documents: JsonElement? = null,
    val status: String? = null,
    @SerializedName("adminNote") val adminNote: String? = null,
    @SerializedName("requiresPayment") val requiresPayment: Boolean = false,
    @SerializedName("paymentAmount") val paymentAmount: Double = 0.0,
    @SerializedName("paymentStatus") val paymentStatus: String? = null,
    @SerializedName("isPaid") val isPaid: Boolean = false,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class ApplicationListResponse(
    val data: List<ApiApplicationDto>
)

data class ApplicationResponse(
    val data: ApiApplicationDto
)

data class ApplicationDocumentResponse(
    val data: ApplicationDocumentData
)

data class ApplicationDocumentData(
    @SerializedName("slotId") val slotId: String,
    @SerializedName("documentType") val documentType: String,
    val url: String
)

data class ApiWalletDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("availableBalance") val availableBalance: Double = 0.0,
    @SerializedName("pendingBalance") val pendingBalance: Double = 0.0,
    @SerializedName("withdrawnBalance") val withdrawnBalance: Double = 0.0,
    @SerializedName("lifetimeEarnings") val lifetimeEarnings: Double = 0.0,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class WalletResponse(
    val data: ApiWalletDto
)

data class ApiWalletTransactionDto(
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("userId") val userId: String,
    val amount: Double,
    val source: String,
    val description: String,
    val reference: String,
    val status: String,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class WalletTransactionListResponse(
    val data: List<ApiWalletTransactionDto>
)

data class ApiReferralDto(
    @SerializedName("referralId") val referralId: String,
    @SerializedName("referrerUserId") val referrerUserId: String,
    @SerializedName("referrerCode") val referrerCode: String,
    @SerializedName("referredUserId") val referredUserId: String,
    @SerializedName("referredUserName") val referredUserName: String,
    @SerializedName("registrationDate") val registrationDate: String? = null,
    @SerializedName("isActive") val isActive: Boolean = true,
    @SerializedName("hasActiveSubscription") val hasActiveSubscription: Boolean = false
)

data class ReferralListResponse(
    val data: List<ApiReferralDto>
)

data class ApiReferralStatsDto(
    @SerializedName("totalReferrals") val totalReferrals: Int = 0,
    @SerializedName("activeReferrals") val activeReferrals: Int = 0,
    @SerializedName("expiredReferrals") val expiredReferrals: Int = 0,
    @SerializedName("totalEarnings") val totalEarnings: Double = 0.0,
    @SerializedName("todayEarnings") val todayEarnings: Double = 0.0,
    @SerializedName("monthlyEarnings") val monthlyEarnings: Double = 0.0,
    @SerializedName("lifetimeEarnings") val lifetimeEarnings: Double = 0.0
)

data class ApiReferralSummaryDto(
    @SerializedName("referralCode") val referralCode: String,
    @SerializedName("referralLink") val referralLink: String,
    val stats: ApiReferralStatsDto,
    val wallet: ApiWalletDto
)

data class ReferralSummaryResponse(
    val data: ApiReferralSummaryDto
)

data class ReferralValidateResponse(
    val data: ReferralValidateData
)

data class ReferralValidateData(
    @SerializedName("referrerUserId") val referrerUserId: String,
    @SerializedName("referralCode") val referralCode: String
)

data class ApiWithdrawalDto(
    @SerializedName("withdrawalId") val withdrawalId: String,
    @SerializedName("userId") val userId: String,
    val amount: Double,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("paymentNumber") val paymentNumber: String,
    val status: String,
    @SerializedName("adminNote") val adminNote: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class WithdrawalListResponse(
    val data: List<ApiWithdrawalDto>
)

data class WithdrawalResponse(
    val data: ApiWithdrawalDto
)

data class CreateWithdrawalRequest(
    val amount: Double,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("payment_number") val paymentNumber: String
)

data class ApiNotificationDto(
    @SerializedName("notificationId") val notificationId: String,
    @SerializedName("userId") val userId: String,
    val title: String,
    val message: String,
    val type: String,
    @SerializedName("referenceId") val referenceId: String? = null,
    @SerializedName("isRead") val isRead: Boolean = false,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class NotificationListResponse(
    val data: List<ApiNotificationDto>
)

data class CreateSubscriptionRequest(
    @SerializedName("plan_id") val planId: String,
    @SerializedName("payment_provider") val paymentProvider: String,
    @SerializedName("payment_reference") val paymentReference: String
)

data class InitializeSubscriptionPaymentRequest(
    @SerializedName("subscription_plan_id") val subscriptionPlanId: String
)

data class InitializeManualPaymentResponse(
    val success: Boolean = false,
    val reference: String = "",
    val data: InitializeManualPaymentData? = null
)

data class InitializeManualPaymentData(
    val reference: String = "",
    val amount: Double = 0.0,
    val currency: String = "GHS",
    @SerializedName("plan_id") val planId: String = "",
    @SerializedName("plan_name") val planName: String = "",
    @SerializedName("service_name") val serviceName: String = "",
    val status: String = "Pending",
    val provider: String = "MANUAL",
    val instructions: ApiManualPaymentDetailsDto? = null
)

data class InitializeSubscriptionPaymentResponse(
    val success: Boolean = false,
    @SerializedName("access_code") val accessCode: String = "",
    @SerializedName("authorization_url") val authorizationUrl: String = "",
    val reference: String = "",
    @SerializedName("public_key") val publicKey: String = "",
    val data: InitializeSubscriptionPaymentData? = null
)

data class InitializeSubscriptionPaymentData(
    @SerializedName("access_code") val accessCode: String = "",
    @SerializedName("authorization_url") val authorizationUrl: String = "",
    val reference: String = "",
    @SerializedName("public_key") val publicKey: String = "",
    val amount: Double = 0.0,
    val currency: String = "GHS",
    @SerializedName(value = "plan_name", alternate = ["service_name", "planName"])
    val planName: String = "",
    val status: String = "Pending"
)

data class SubscriptionPaymentStatusResponse(
    val success: Boolean = false,
    val data: ApiPaymentStatusDto
)

data class ApiPaymentStatusDto(
    val paymentId: String? = null,
    val reference: String,
    val status: String,
    val amount: Double = 0.0,
    val currency: String = "GHS",
    val provider: String? = null,
    val planId: String? = null,
    val planName: String? = null,
    val subscriptionId: String? = null,
    val subscriptionActive: Boolean = false
)

data class ApiSubscriptionDto(
    @SerializedName("subscriptionId") val subscriptionId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("planId") val planId: String,
    @SerializedName("planName") val planName: String,
    val price: Double,
    @SerializedName("durationDays") val durationDays: Int,
    @SerializedName("paymentProvider") val paymentProvider: String,
    @SerializedName("paymentReference") val paymentReference: String,
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    val status: String,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class SubscriptionResponse(
    val data: ApiSubscriptionDto
)

data class AccessLevelResponse(
    val data: AccessLevelData
)

data class AccessLevelData(
    @SerializedName("accessLevel") val accessLevel: String
)
