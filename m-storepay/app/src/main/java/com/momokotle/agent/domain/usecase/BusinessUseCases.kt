package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.domain.repository.BusinessSettingsRepository
import com.momokotle.agent.domain.repository.NotificationRepository
import com.momokotle.agent.domain.repository.ReferralRepository
import com.momokotle.agent.domain.repository.SubscriptionRepository
import com.momokotle.agent.domain.repository.WalletRepository
import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.AppNotification
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.ReferralInfo
import com.momokotle.agent.models.ReferralRecord
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.UserSubscription
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WithdrawalRequest
import com.momokotle.agent.utils.PhoneNumberFormatter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val referralRepository: ReferralRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        location: String,
        password: String,
        confirmPassword: String,
        referralCode: String? = null
    ): AuthResult {
        val first = firstName.trim()
        val last = lastName.trim()
        val mail = email.trim()
        val loc = location.trim()
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber)
        val refCode = referralCode?.trim()?.takeIf { it.isNotBlank() }

        when {
            first.isBlank() -> return AuthResult.Error("First name is required")
            last.isBlank() -> return AuthResult.Error("Last name is required")
            mail.isBlank() -> return AuthResult.Error("Email is required")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches() ->
                return AuthResult.Error("Enter a valid email address")
            phone.isBlank() -> return AuthResult.Error("Phone number is required")
            !PhoneNumberFormatter.isValidGhanaNumber(phone) ->
                return AuthResult.Error("Enter a valid Ghana phone number")
            loc.isBlank() -> return AuthResult.Error("Location is required")
            password.length < 6 -> return AuthResult.Error("Password must be at least 6 characters")
            password != confirmPassword -> return AuthResult.Error("Passwords do not match")
        }

        if (refCode != null) {
            referralRepository.validateReferralCode(refCode).getOrElse { error ->
                return AuthResult.Error(error.message ?: "Invalid referral code")
            }
        }

        return authRepository.register(
            firstName = first,
            lastName = last,
            email = mail,
            phoneNumber = phone,
            location = loc,
            password = password,
            referralCode = refCode
        )
    }
}

class VerifyRegistrationOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, otpCode: String): AuthResult {
        val code = otpCode.trim()
        when {
            email.isBlank() -> return AuthResult.Error("Email is required")
            code.length != 6 || code.any { !it.isDigit() } ->
                return AuthResult.Error("Enter the 6-digit verification code")
        }
        return authRepository.verifyRegistrationOtp(email.trim(), code)
    }
}

class ResendRegistrationOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult {
        if (email.isBlank()) {
            return AuthResult.Error("Email is required")
        }
        return authRepository.resendRegistrationOtp(email.trim())
    }
}

class ObserveReferralInfoUseCase @Inject constructor(
    private val referralRepository: ReferralRepository
) {
    operator fun invoke(userId: String): Flow<ReferralInfo?> =
        referralRepository.observeReferralInfo(userId)
}

class ObserveReferralsUseCase @Inject constructor(
    private val referralRepository: ReferralRepository
) {
    operator fun invoke(userId: String): Flow<List<ReferralRecord>> =
        referralRepository.observeReferrals(userId)
}

class ObserveWalletUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(userId: String): Flow<Wallet?> = walletRepository.observeWallet(userId)
}

class ObserveWalletTransactionsUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(userId: String): Flow<List<WalletTransactionRecord>> =
        walletRepository.observeTransactions(userId)
}

class RequestWithdrawalUseCase @Inject constructor(
    private val walletRepository: WalletRepository,
    private val businessSettingsRepository: BusinessSettingsRepository
) {
    suspend operator fun invoke(
        userId: String,
        amount: Double,
        paymentMethod: PaymentProvider,
        paymentNumber: String
    ): Result<WithdrawalRequest> {
        val settings = businessSettingsRepository.getSettings()
        if (amount < settings.minimumWithdrawal) {
            return Result.failure(
                IllegalArgumentException("Minimum withdrawal is ${settings.currency} ${settings.minimumWithdrawal}")
            )
        }
        return walletRepository.requestWithdrawal(userId, amount, paymentMethod, paymentNumber)
    }
}

class ObserveWithdrawalsUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(userId: String): Flow<List<WithdrawalRequest>> =
        walletRepository.observeWithdrawals(userId)
}

class ObserveSubscriptionPlansUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    operator fun invoke(): Flow<List<SubscriptionPlan>> = subscriptionRepository.observePlans()
}

class PurchaseSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(
        userId: String,
        planId: String,
        provider: PaymentProvider,
        paymentReference: String
    ): Result<UserSubscription> =
        subscriptionRepository.purchaseSubscription(userId, planId, provider, paymentReference)
}

class InitializeSubscriptionPaymentUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(planId: String) = subscriptionRepository.initializePayment(planId)
}

class InitializeManualSubscriptionPaymentUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(planId: String): Result<ManualPaymentCheckout> =
        subscriptionRepository.initializeManualPayment(planId)
}

class CheckSubscriptionPaymentStatusUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(reference: String) = subscriptionRepository.checkPaymentStatus(reference)
}

class CheckAccessLevelUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(userId: String): AccessLevel =
        subscriptionRepository.checkAccessLevel(userId)
}

class ObserveBusinessSettingsUseCase @Inject constructor(
    private val businessSettingsRepository: BusinessSettingsRepository
) {
    operator fun invoke(): Flow<BusinessSettings> = businessSettingsRepository.observeSettings()
}

class ObserveNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(userId: String): Flow<List<AppNotification>> =
        notificationRepository.observeNotifications(userId)
}

class MarkNotificationReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String) =
        notificationRepository.markAsRead(notificationId)
}

class MarkAllNotificationsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String) =
        notificationRepository.markAllAsRead(userId)
}
