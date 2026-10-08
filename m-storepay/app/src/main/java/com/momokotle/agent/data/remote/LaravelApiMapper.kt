package com.momokotle.agent.data.remote

import com.google.gson.Gson
import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.AppNotification
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.ManualPaymentDetails
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatus
import com.momokotle.agent.models.NotificationType
import com.momokotle.agent.models.OrderStatus
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.PlanStatus
import com.momokotle.agent.models.ReferralInfo
import com.momokotle.agent.models.ReferralRecord
import com.momokotle.agent.models.ReferralStats
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.SubscriptionRecordStatus
import com.momokotle.agent.models.SubscriptionStatus
import com.momokotle.agent.models.UserSubscription
import com.momokotle.agent.models.UserProfile
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WalletTransactionSource
import com.momokotle.agent.models.WalletTransactionStatus
import com.momokotle.agent.models.WithdrawalRequest
import com.momokotle.agent.models.WithdrawalStatus
import com.momokotle.agent.models.firestoreCategoryToStoreCategory
import com.momokotle.agent.utils.JsonStringMaps
import com.momokotle.agent.utils.WhatsAppSupportHelper
import retrofit2.HttpException
import java.time.Instant
import java.time.format.DateTimeParseException

object LaravelApiMapper {

    fun mapUser(dto: ApiUserDto): UserProfile {
        val firstName = dto.firstName.orEmpty()
        val lastName = dto.lastName.orEmpty()
        val fullName = dto.fullName?.takeIf { it.isNotBlank() }
            ?: listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")

        return UserProfile(
            userId = dto.id.toString(),
            firstName = firstName,
            lastName = lastName,
            fullName = fullName,
            email = dto.email,
            phoneNumber = dto.phoneNumber.orEmpty(),
            location = dto.location.orEmpty(),
            accountStatus = dto.accountStatus ?: UserProfile.ACCOUNT_STATUS_ACTIVE,
            referralCode = dto.referralCode.orEmpty(),
            trialEndDate = parseIsoMillis(dto.trialEndDate),
            subscriptionStatus = SubscriptionStatus.fromFirestore(dto.subscriptionStatus),
            subscriptionExpiryDate = parseIsoMillis(dto.subscriptionExpiryDate),
            serverAccessLevel = mapAccessLevel(dto.accessLevel)
        )
    }

    fun mapProduct(dto: ApiProductDto): StoreProduct? {
        val category = firestoreCategoryToStoreCategory(dto.category) ?: return null
        return StoreProduct(
            productId = dto.id,
            productName = dto.productName,
            productDescription = dto.productDescription.orEmpty(),
            category = category,
            price = dto.price,
            stockQuantity = dto.stockQuantity,
            productImageUrl = dto.productImageUrl?.takeIf { it.isNotBlank() },
            isAvailable = dto.isAvailable,
            updatedAt = parseIsoMillis(dto.updatedAt) ?: 0L
        )
    }

    fun mapSubscriptionPlan(dto: ApiSubscriptionPlanDto): SubscriptionPlan =
        SubscriptionPlan(
            planId = dto.id,
            planName = dto.planName,
            price = dto.price,
            durationDays = dto.durationDays,
            status = PlanStatus.fromFirestore(dto.status),
            sortOrder = dto.sortOrder
        )

    fun mapBusinessSettings(dto: ApiBusinessSettingsDto): BusinessSettings =
        BusinessSettings(
            minimumWithdrawal = dto.minimumWithdrawal,
            largeTransactionWarningAmount = dto.largeTransactionWarningAmount,
            trialDays = dto.trialDays,
            currency = dto.currency,
            taxRate = dto.taxRate,
            storeEnabled = dto.storeEnabled,
            subscriptionEnabled = dto.subscriptionEnabled,
            referralShareLinkBase = dto.referralShareLinkBase,
            supportWhatsAppNumber = WhatsAppSupportHelper.normalizeDigits(dto.supportWhatsAppNumber.orEmpty()).orEmpty(),
            paystackPaymentEnabled = dto.paystackPaymentEnabled,
            manualPaymentEnabled = dto.manualPaymentEnabled,
            manualPayment = mapManualPaymentDetails(dto.manualPayment)
        )

    fun mapManualPaymentDetails(dto: ApiManualPaymentDetailsDto?): ManualPaymentDetails =
        ManualPaymentDetails(
            accountName = dto?.accountName.orEmpty(),
            accountNumber = dto?.accountNumber.orEmpty(),
            bankName = dto?.bankName.orEmpty(),
            momoNetwork = dto?.momoNetwork.orEmpty(),
            momoNumber = dto?.momoNumber.orEmpty(),
            instructions = dto?.instructions.orEmpty(),
            supportContact = dto?.supportContact.orEmpty()
        )

    fun mapOrder(dto: ApiOrderDto): StoreOrder =
        StoreOrder(
            orderId = dto.id,
            userId = dto.userId,
            agentName = dto.agentName,
            itemName = dto.itemName,
            quantity = dto.quantity,
            totalAmount = dto.totalAmount,
            phoneNumber = dto.phoneNumber,
            location = dto.location,
            status = OrderStatus.fromFirestore(dto.status),
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L,
            updatedAt = parseIsoMillis(dto.updatedAt)
                ?: parseIsoMillis(dto.createdAt)
                ?: 0L
        )

    fun mapApplication(dto: ApiApplicationDto): FormSubmission? {
        val submissionId = dto.id?.takeIf { it.isNotBlank() } ?: return null
        val formType = AgentFormType.fromDisplayName(dto.formType.orEmpty()) ?: return null
        return FormSubmission(
            submissionId = submissionId,
            userId = dto.userId.orEmpty(),
            formType = formType,
            applicantName = dto.applicantName.orEmpty(),
            phoneNumber = dto.phoneNumber.orEmpty(),
            formData = JsonStringMaps.fromElement(dto.formData),
            uploadedDocuments = JsonStringMaps.fromElement(dto.documents),
            status = FormSubmissionStatus.fromFirestore(dto.status),
            adminNote = dto.adminNote?.takeIf { it.isNotBlank() },
            requiresPayment = dto.requiresPayment,
            paymentAmount = dto.paymentAmount,
            paymentStatus = dto.paymentStatus,
            isPaid = dto.isPaid || dto.paymentStatus.equals("Confirmed", ignoreCase = true),
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L,
            updatedAt = parseIsoMillis(dto.updatedAt)
                ?: parseIsoMillis(dto.createdAt)
                ?: 0L
        )
    }

    fun mapWallet(dto: ApiWalletDto): Wallet =
        Wallet(
            userId = dto.userId,
            availableBalance = dto.availableBalance,
            pendingBalance = dto.pendingBalance,
            withdrawnBalance = dto.withdrawnBalance,
            lifetimeEarnings = dto.lifetimeEarnings,
            updatedAt = parseIsoMillis(dto.updatedAt) ?: System.currentTimeMillis()
        )

    fun mapWalletTransaction(dto: ApiWalletTransactionDto): WalletTransactionRecord =
        WalletTransactionRecord(
            transactionId = dto.transactionId,
            userId = dto.userId,
            amount = dto.amount,
            source = WalletTransactionSource.fromFirestore(dto.source),
            description = dto.description,
            reference = dto.reference,
            status = WalletTransactionStatus.fromFirestore(dto.status),
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L
        )

    fun mapReferral(dto: ApiReferralDto): ReferralRecord =
        ReferralRecord(
            referralId = dto.referralId,
            referrerUserId = dto.referrerUserId,
            referrerCode = dto.referrerCode,
            referredUserId = dto.referredUserId,
            referredUserName = dto.referredUserName,
            registrationDate = parseIsoMillis(dto.registrationDate) ?: 0L,
            isActive = dto.isActive,
            hasActiveSubscription = dto.hasActiveSubscription
        )

    fun mapReferralSummary(dto: ApiReferralSummaryDto): ReferralInfo =
        ReferralInfo(
            referralCode = dto.referralCode,
            referralLink = dto.referralLink,
            stats = ReferralStats(
                totalReferrals = dto.stats.totalReferrals,
                activeReferrals = dto.stats.activeReferrals,
                expiredReferrals = dto.stats.expiredReferrals,
                totalEarnings = dto.stats.totalEarnings,
                todayEarnings = dto.stats.todayEarnings,
                monthlyEarnings = dto.stats.monthlyEarnings,
                lifetimeEarnings = dto.stats.lifetimeEarnings
            ),
            wallet = mapWallet(dto.wallet)
        )

    fun mapWithdrawal(dto: ApiWithdrawalDto): WithdrawalRequest =
        WithdrawalRequest(
            withdrawalId = dto.withdrawalId,
            userId = dto.userId,
            amount = dto.amount,
            paymentMethod = PaymentProvider.fromFirestore(dto.paymentMethod) ?: PaymentProvider.MTN_MOMO,
            paymentNumber = dto.paymentNumber,
            status = WithdrawalStatus.fromFirestore(dto.status),
            adminNote = dto.adminNote,
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L,
            updatedAt = parseIsoMillis(dto.updatedAt) ?: 0L
        )

    fun mapNotification(dto: ApiNotificationDto): AppNotification =
        AppNotification(
            notificationId = dto.notificationId,
            userId = dto.userId,
            title = dto.title,
            message = dto.message,
            type = NotificationType.fromFirestore(dto.type),
            referenceId = dto.referenceId,
            isRead = dto.isRead,
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L
        )

    fun mapSubscription(dto: ApiSubscriptionDto): UserSubscription =
        UserSubscription(
            subscriptionId = dto.subscriptionId,
            userId = dto.userId,
            planId = dto.planId,
            planName = dto.planName,
            price = dto.price,
            durationDays = dto.durationDays,
            paymentProvider = PaymentProvider.fromFirestore(dto.paymentProvider) ?: PaymentProvider.PAYSTACK,
            paymentReference = dto.paymentReference,
            startDate = parseIsoMillis(dto.startDate) ?: 0L,
            endDate = parseIsoMillis(dto.endDate) ?: 0L,
            status = SubscriptionRecordStatus.fromFirestore(dto.status),
            createdAt = parseIsoMillis(dto.createdAt) ?: 0L
        )

    fun mapManualPaymentCheckout(response: InitializeManualPaymentResponse): ManualPaymentCheckout {
        val data = response.data
        val name = data?.planName?.takeIf { it.isNotBlank() }
            ?: data?.serviceName?.takeIf { it.isNotBlank() }
            ?: ""
        return ManualPaymentCheckout(
            reference = response.reference.ifBlank { data?.reference.orEmpty() },
            amount = data?.amount ?: 0.0,
            currency = data?.currency ?: "GHS",
            planId = data?.planId.orEmpty(),
            planName = name,
            status = data?.status ?: "Pending",
            instructions = mapManualPaymentDetails(data?.instructions)
        )
    }

    fun mapSubscriptionCheckout(response: InitializeSubscriptionPaymentResponse): SubscriptionCheckout =
        SubscriptionCheckout(
            accessCode = response.accessCode.ifBlank { response.data?.accessCode.orEmpty() },
            authorizationUrl = response.authorizationUrl.ifBlank { response.data?.authorizationUrl.orEmpty() },
            reference = response.reference.ifBlank { response.data?.reference.orEmpty() },
            publicKey = response.publicKey.ifBlank { response.data?.publicKey.orEmpty() },
            amount = response.data?.amount ?: 0.0,
            currency = response.data?.currency ?: "GHS",
            planName = response.data?.planName.orEmpty(),
            status = response.data?.status ?: "Pending"
        )

    fun mapSubscriptionPaymentStatus(dto: ApiPaymentStatusDto): SubscriptionPaymentStatus =
        SubscriptionPaymentStatus(
            reference = dto.reference,
            status = dto.status,
            amount = dto.amount,
            currency = dto.currency,
            planName = dto.planName,
            subscriptionActive = dto.subscriptionActive,
            provider = dto.provider
        )

    fun mapAccessLevel(value: String?): AccessLevel =
        when (value?.trim()?.lowercase()) {
            "full" -> AccessLevel.FULL
            "subscription_required" -> AccessLevel.SUBSCRIPTION_REQUIRED
            else -> AccessLevel.LIMITED
        }

    fun parseApiError(error: Throwable, gson: Gson = Gson()): String {
        if (error is HttpException) {
            val body = error.response()?.errorBody()?.string()
            if (!body.isNullOrBlank()) {
                runCatching {
                    val parsed = gson.fromJson(body, ApiErrorBody::class.java)
                    parsed.message?.takeIf { it.isNotBlank() }
                        ?: parsed.errors?.values?.flatten()?.firstOrNull()
                }.getOrNull()?.let { return it }
            }
            return when (error.code()) {
                401 -> "Invalid email or password."
                403 -> "Your account cannot access the app. Please contact support."
                422 -> "Please check your details and try again."
                in 500..599 -> "Server error. Please try again later."
                else -> "Network error. Please try again."
            }
        }
        return error.message ?: "Something went wrong. Please try again."
    }

    fun parseIsoMillis(value: String?): Long? {
        if (value.isNullOrBlank()) return null
        val trimmed = value.trim()
        return try {
            Instant.parse(trimmed).toEpochMilli()
        } catch (_: DateTimeParseException) {
            try {
                java.time.OffsetDateTime.parse(trimmed).toInstant().toEpochMilli()
            } catch (_: DateTimeParseException) {
                try {
                    java.time.LocalDateTime.parse(trimmed).toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
                } catch (_: DateTimeParseException) {
                    null
                }
            }
        }
    }
}
