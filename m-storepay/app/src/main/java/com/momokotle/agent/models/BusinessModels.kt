package com.momokotle.agent.models

/**
 * Admin-managed business configuration from Laravel.
 */
data class BusinessSettings(
    val minimumWithdrawal: Double = 10.0,
    /** Agents must confirm before dialing when amount is this value or higher (GHS). */
    val largeTransactionWarningAmount: Double = 1_000.0,
    val trialDays: Int = 7,
    val currency: String = "GHS",
    val taxRate: Double = 0.0,
    val storeEnabled: Boolean = true,
    val subscriptionEnabled: Boolean = true,
    val referralShareLinkBase: String = "https://momokotle.app/ref/",
    /** Digits-only international WhatsApp number from Laravel (e.g. 233244123456). */
    val supportWhatsAppNumber: String = "",
    val paystackPaymentEnabled: Boolean = true,
    val manualPaymentEnabled: Boolean = true,
    val manualPayment: ManualPaymentDetails = ManualPaymentDetails()
) {
    val canPayWithPaystack: Boolean get() = subscriptionEnabled
    val canPayManually: Boolean get() = subscriptionEnabled
    val hasWhatsAppSupport: Boolean get() = supportWhatsAppNumber.isNotBlank()
}

data class ManualPaymentDetails(
    val accountName: String = "",
    val accountNumber: String = "",
    val bankName: String = "",
    val momoNetwork: String = "",
    val momoNumber: String = "",
    val instructions: String = "",
    val supportContact: String = ""
) {
    val hasDetails: Boolean
        get() = accountNumber.isNotBlank() || momoNumber.isNotBlank() || instructions.isNotBlank()
}

enum class SubscriptionStatus(val firestoreValue: String) {
    NONE("None"),
    TRIAL("Trial"),
    ACTIVE("Active"),
    EXPIRED("Expired");

    companion object {
        fun fromFirestore(value: String?): SubscriptionStatus = when (value?.trim()) {
            "Trial", "TRIAL" -> TRIAL
            "Active", "ACTIVE" -> ACTIVE
            "Expired", "EXPIRED" -> EXPIRED
            else -> NONE
        }
    }
}

enum class AccessLevel {
    FULL,
    LIMITED,
    SUBSCRIPTION_REQUIRED
}

data class UserBusinessProfile(
    val userId: String,
    val firstName: String = "",
    val lastName: String = "",
    val referralCode: String = "",
    val referredBy: String? = null,
    val referredByUserId: String? = null,
    val referralDate: Long? = null,
    val trialStartDate: Long? = null,
    val trialEndDate: Long? = null,
    val isTrialUsed: Boolean = false,
    val subscriptionStatus: SubscriptionStatus = SubscriptionStatus.NONE,
    val subscriptionExpiryDate: Long? = null,
    val activeSubscriptionId: String? = null,
    val serverAccessLevel: AccessLevel? = null
) {
    val fullName: String
        get() = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")

    fun resolveAccessLevel(nowMillis: Long = System.currentTimeMillis()): AccessLevel {
        val trialLive = subscriptionStatus == SubscriptionStatus.TRIAL &&
            trialEndDate != null && nowMillis < trialEndDate
        val paidLive = subscriptionStatus == SubscriptionStatus.ACTIVE &&
            subscriptionExpiryDate != null && nowMillis < subscriptionExpiryDate
        val paidWithoutParsedExpiry = subscriptionStatus == SubscriptionStatus.ACTIVE &&
            subscriptionExpiryDate == null
        if (trialLive || paidLive || paidWithoutParsedExpiry) return AccessLevel.FULL

        val trialEnded = subscriptionStatus == SubscriptionStatus.TRIAL &&
            trialEndDate != null && nowMillis >= trialEndDate
        val paidEnded = subscriptionStatus == SubscriptionStatus.ACTIVE &&
            subscriptionExpiryDate != null && nowMillis >= subscriptionExpiryDate
        if (subscriptionStatus == SubscriptionStatus.EXPIRED || trialEnded || paidEnded) {
            return AccessLevel.SUBSCRIPTION_REQUIRED
        }
        if (serverAccessLevel == AccessLevel.FULL) return AccessLevel.FULL
        if (serverAccessLevel == AccessLevel.SUBSCRIPTION_REQUIRED) {
            return AccessLevel.SUBSCRIPTION_REQUIRED
        }
        return AccessLevel.LIMITED
    }
}

data class SubscriptionPlan(
    val planId: String,
    val planName: String,
    val price: Double,
    val durationDays: Int,
    val status: PlanStatus = PlanStatus.ACTIVE,
    val sortOrder: Int = 0
)

enum class PlanStatus(val firestoreValue: String) {
    ACTIVE("Active"),
    DISABLED("Disabled");

    companion object {
        fun fromFirestore(value: String?): PlanStatus = when (value?.trim()) {
            "Disabled", "DISABLED" -> DISABLED
            else -> ACTIVE
        }
    }
}

data class ManualPaymentCheckout(
    val reference: String,
    val amount: Double,
    val currency: String,
    val planId: String,
    val planName: String,
    val status: String,
    val instructions: ManualPaymentDetails
)

data class SubscriptionCheckout(
    val accessCode: String,
    val authorizationUrl: String,
    val reference: String,
    val publicKey: String,
    val amount: Double,
    val currency: String,
    val planName: String,
    val status: String
) {
    val checkoutUrl: String
        get() = authorizationUrl.ifBlank {
            accessCode.takeIf { it.isNotBlank() }?.let { "https://checkout.paystack.com/$it" }.orEmpty()
        }
}

data class SubscriptionPaymentStatus(
    val reference: String,
    val status: String,
    val amount: Double,
    val currency: String,
    val planName: String? = null,
    val subscriptionActive: Boolean = false,
    val provider: String? = null
) {
    val isConfirmed: Boolean get() = status.equals("Confirmed", ignoreCase = true)
    val isPending: Boolean get() =
        status.equals("Pending", ignoreCase = true) || status.equals("Ongoing", ignoreCase = true)
    val isUnsuccessful: Boolean get() =
        status.equals("Failed", ignoreCase = true) ||
            status.equals("Abandoned", ignoreCase = true) ||
            status.equals("Reversed", ignoreCase = true)
}

data class UserSubscription(
    val subscriptionId: String,
    val userId: String,
    val planId: String,
    val planName: String,
    val price: Double,
    val durationDays: Int,
    val paymentProvider: PaymentProvider,
    val paymentReference: String,
    val startDate: Long,
    val endDate: Long,
    val status: SubscriptionRecordStatus = SubscriptionRecordStatus.ACTIVE,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SubscriptionRecordStatus(val firestoreValue: String) {
    ACTIVE("Active"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled");

    companion object {
        fun fromFirestore(value: String?): SubscriptionRecordStatus = when (value?.trim()) {
            "Expired", "EXPIRED" -> EXPIRED
            "Cancelled", "CANCELLED" -> CANCELLED
            else -> ACTIVE
        }
    }
}

enum class PaymentProvider(val firestoreValue: String, val displayName: String) {
    PAYSTACK("PAYSTACK", "Paystack"),
    MTN_MOMO("MTN_MOMO", "MTN MoMo"),
    TELECEL_CASH("TELECEL_CASH", "Telecel Cash"),
    AT_MONEY("AT_MONEY", "AT Money");

    companion object {
        val walletProviders: List<PaymentProvider> = entries.filter { it != PAYSTACK }

        fun fromFirestore(value: String?): PaymentProvider? = entries.find {
            it.firestoreValue.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
        }
    }
}

data class Wallet(
    val userId: String,
    val availableBalance: Double = 0.0,
    val pendingBalance: Double = 0.0,
    val withdrawnBalance: Double = 0.0,
    val lifetimeEarnings: Double = 0.0,
    val updatedAt: Long = System.currentTimeMillis()
)

enum class WalletTransactionSource(val firestoreValue: String) {
    COMMISSION_EARNED("CommissionEarned"),
    COMMISSION_REVERSED("CommissionReversed"),
    WITHDRAWAL_REQUESTED("WithdrawalRequested"),
    WITHDRAWAL_REJECTED("WithdrawalRejected"),
    WITHDRAWAL_PAID("WithdrawalPaid"),
    WITHDRAWAL("Withdrawal"),
    ADJUSTMENT("Adjustment"),
    REFUND("Refund");

    companion object {
        fun fromFirestore(value: String?): WalletTransactionSource = entries.find {
            it.firestoreValue.equals(value, ignoreCase = true)
        } ?: ADJUSTMENT
    }
}

enum class WalletTransactionStatus(val firestoreValue: String) {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled");

    companion object {
        fun fromFirestore(value: String?): WalletTransactionStatus = when (value?.trim()) {
            "Pending", "PENDING" -> PENDING
            "Failed", "FAILED" -> FAILED
            "Cancelled", "CANCELLED" -> CANCELLED
            else -> COMPLETED
        }
    }
}

data class WalletTransactionRecord(
    val transactionId: String,
    val userId: String,
    val amount: Double,
    val source: WalletTransactionSource,
    val description: String,
    val reference: String,
    val status: WalletTransactionStatus,
    val createdAt: Long = System.currentTimeMillis()
)

data class ReferralRecord(
    val referralId: String,
    val referrerUserId: String,
    val referrerCode: String,
    val referredUserId: String,
    val referredUserName: String,
    val registrationDate: Long,
    val isActive: Boolean = true,
    val hasActiveSubscription: Boolean = false
)

data class ReferralStats(
    val totalReferrals: Int = 0,
    val activeReferrals: Int = 0,
    val expiredReferrals: Int = 0,
    val totalEarnings: Double = 0.0,
    val todayEarnings: Double = 0.0,
    val monthlyEarnings: Double = 0.0,
    val lifetimeEarnings: Double = 0.0
)

data class RevenueTransaction(
    val revenueId: String,
    val source: String,
    val amount: Double,
    val orderId: String? = null,
    val subscriptionId: String? = null,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)

enum class WithdrawalStatus(val firestoreValue: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    PAID("Paid");

    companion object {
        fun fromFirestore(value: String?): WithdrawalStatus = when (value?.trim()) {
            "Approved", "APPROVED" -> APPROVED
            "Rejected", "REJECTED" -> REJECTED
            "Paid", "PAID" -> PAID
            else -> PENDING
        }
    }
}

data class WithdrawalRequest(
    val withdrawalId: String,
    val userId: String,
    val amount: Double,
    val paymentMethod: PaymentProvider,
    val paymentNumber: String,
    val status: WithdrawalStatus = WithdrawalStatus.PENDING,
    val adminNote: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class AppNotification(
    val notificationId: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val referenceId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class NotificationType(val firestoreValue: String) {
    REFERRAL_JOINED("ReferralJoined"),
    COMMISSION_EARNED("CommissionEarned"),
    SUBSCRIPTION_EXPIRED("SubscriptionExpired"),
    WITHDRAWAL_APPROVED("WithdrawalApproved"),
    ORDER_COMPLETED("OrderCompleted"),
    GENERAL("General");

    companion object {
        fun fromFirestore(value: String?): NotificationType = entries.find {
            it.firestoreValue.equals(value, ignoreCase = true)
        } ?: GENERAL
    }
}

data class PaymentRecord(
    val paymentId: String,
    val userId: String,
    val amount: Double,
    val provider: PaymentProvider,
    val reference: String,
    val purpose: PaymentPurpose,
    val purposeId: String,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PaymentPurpose(val firestoreValue: String) {
    SUBSCRIPTION("Subscription"),
    STORE_ORDER("StoreOrder");

    companion object {
        fun fromFirestore(value: String?): PaymentPurpose = entries.find {
            it.firestoreValue.equals(value, ignoreCase = true)
        } ?: SUBSCRIPTION
    }
}

enum class PaymentStatus(val firestoreValue: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    FAILED("Failed");

    companion object {
        fun fromFirestore(value: String?): PaymentStatus = when (value?.trim()) {
            "Confirmed", "CONFIRMED" -> CONFIRMED
            "Failed", "FAILED" -> FAILED
            else -> PENDING
        }
    }
}
