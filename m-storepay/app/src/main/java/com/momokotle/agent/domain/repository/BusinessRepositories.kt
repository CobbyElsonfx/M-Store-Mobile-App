package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.AppNotification
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.ReferralInfo
import com.momokotle.agent.models.ReferralRecord
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.UserSubscription
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WithdrawalRequest
import kotlinx.coroutines.flow.Flow

interface BusinessSettingsRepository {
    suspend fun getSettings(): BusinessSettings
    fun observeSettings(): Flow<BusinessSettings>
}

interface ReferralRepository {
    fun observeReferralInfo(userId: String): Flow<ReferralInfo?>
    suspend fun getReferralInfo(userId: String): ReferralInfo?
    fun observeReferrals(userId: String): Flow<List<ReferralRecord>>
    suspend fun validateReferralCode(code: String): Result<String>
}

interface WalletRepository {
    fun observeWallet(userId: String): Flow<Wallet?>
    fun observeTransactions(userId: String): Flow<List<WalletTransactionRecord>>
    suspend fun requestWithdrawal(
        userId: String,
        amount: Double,
        paymentMethod: PaymentProvider,
        paymentNumber: String
    ): Result<WithdrawalRequest>
    fun observeWithdrawals(userId: String): Flow<List<WithdrawalRequest>>
}

interface SubscriptionRepository {
    fun observePlans(): Flow<List<SubscriptionPlan>>
    suspend fun getPlans(): List<SubscriptionPlan>
    suspend fun purchaseSubscription(
        userId: String,
        planId: String,
        provider: PaymentProvider,
        paymentReference: String
    ): Result<UserSubscription>
    suspend fun initializePayment(planId: String): Result<SubscriptionCheckout>
    suspend fun initializeManualPayment(planId: String): Result<ManualPaymentCheckout>
    suspend fun checkPaymentStatus(reference: String): Result<SubscriptionPaymentStatus>
    suspend fun checkAccessLevel(userId: String): AccessLevel
}

interface NotificationRepository {
    fun observeNotifications(userId: String): Flow<List<AppNotification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead(userId: String)
}
