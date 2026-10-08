package com.momokotle.agent.repository

import com.momokotle.agent.data.remote.LaravelApiMapper
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
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.UserSubscription
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WithdrawalRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessSettingsRepositoryImpl @Inject constructor(
    private val remoteDataSource: com.momokotle.agent.data.remote.LaravelBusinessSettingsRemoteDataSource
) : BusinessSettingsRepository {
    override suspend fun getSettings(): BusinessSettings = remoteDataSource.fetchSettings()

    override fun observeSettings(): Flow<BusinessSettings> = kotlinx.coroutines.flow.flow {
        while (true) {
            emit(
                runCatching { remoteDataSource.fetchSettings() }
                    .getOrDefault(BusinessSettings())
            )
            kotlinx.coroutines.delay(SETTINGS_REFRESH_MS)
        }
    }

    companion object {
        private const val SETTINGS_REFRESH_MS = 60_000L
    }
}

@Singleton
class ReferralRepositoryImpl @Inject constructor(
    private val referralRemoteDataSource: com.momokotle.agent.data.remote.LaravelReferralRemoteDataSource
) : ReferralRepository {

    override fun observeReferralInfo(userId: String): Flow<ReferralInfo?> = pollingFlow {
        referralRemoteDataSource.fetchSummary()
    }

    override suspend fun getReferralInfo(userId: String): ReferralInfo? =
        runCatching { referralRemoteDataSource.fetchSummary() }.getOrNull()

    override fun observeReferrals(userId: String): Flow<List<ReferralRecord>> = pollingListFlow {
        referralRemoteDataSource.fetchReferrals()
    }

    override suspend fun validateReferralCode(code: String): Result<String> {
        return try {
            Result.success(referralRemoteDataSource.validateReferralCode(code.trim()))
        } catch (error: Exception) {
            Result.failure(IllegalArgumentException(LaravelApiMapper.parseApiError(error)))
        }
    }
}

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val walletRemoteDataSource: com.momokotle.agent.data.remote.LaravelWalletRemoteDataSource
) : WalletRepository {

    override fun observeWallet(userId: String): Flow<Wallet?> = pollingFlow {
        walletRemoteDataSource.fetchWallet()
    }

    override fun observeTransactions(userId: String): Flow<List<WalletTransactionRecord>> =
        pollingListFlow { walletRemoteDataSource.fetchTransactions() }

    override suspend fun requestWithdrawal(
        userId: String,
        amount: Double,
        paymentMethod: PaymentProvider,
        paymentNumber: String
    ): Result<WithdrawalRequest> = try {
        Result.success(
            walletRemoteDataSource.requestWithdrawal(
                amount = amount,
                paymentMethod = paymentMethod,
                paymentNumber = paymentNumber
            )
        )
    } catch (error: Exception) {
        Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
    }

    override fun observeWithdrawals(userId: String): Flow<List<WithdrawalRequest>> =
        pollingListFlow { walletRemoteDataSource.fetchWithdrawals() }
}

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val planRemoteDataSource: com.momokotle.agent.data.remote.LaravelSubscriptionPlanRemoteDataSource,
    private val subscriptionRemoteDataSource: com.momokotle.agent.data.remote.LaravelSubscriptionRemoteDataSource
) : SubscriptionRepository {

    override fun observePlans(): Flow<List<SubscriptionPlan>> = kotlinx.coroutines.flow.flow {
        while (true) {
            emit(
                runCatching { planRemoteDataSource.fetchActivePlans() }
                    .getOrDefault(emptyList())
            )
            kotlinx.coroutines.delay(PLANS_REFRESH_MS)
        }
    }

    override suspend fun getPlans(): List<SubscriptionPlan> = planRemoteDataSource.fetchActivePlans()

    override suspend fun purchaseSubscription(
        userId: String,
        planId: String,
        provider: PaymentProvider,
        paymentReference: String
    ): Result<UserSubscription> = try {
        Result.success(
            subscriptionRemoteDataSource.purchaseSubscription(
                planId = planId,
                provider = provider,
                paymentReference = paymentReference
            )
        )
    } catch (error: Exception) {
        Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
    }

    override suspend fun initializePayment(planId: String): Result<SubscriptionCheckout> = try {
        Result.success(subscriptionRemoteDataSource.initializePayment(planId))
    } catch (error: Exception) {
        Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
    }

    override suspend fun initializeManualPayment(planId: String): Result<ManualPaymentCheckout> = try {
        Result.success(subscriptionRemoteDataSource.initializeManualPayment(planId))
    } catch (error: Exception) {
        Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
    }

    override suspend fun checkPaymentStatus(reference: String): Result<SubscriptionPaymentStatus> = try {
        Result.success(subscriptionRemoteDataSource.fetchPaymentStatus(reference))
    } catch (error: Exception) {
        Result.failure(IllegalStateException(LaravelApiMapper.parseApiError(error)))
    }

    override suspend fun checkAccessLevel(userId: String): AccessLevel =
        runCatching { subscriptionRemoteDataSource.fetchAccessLevel() }
            .getOrDefault(AccessLevel.LIMITED)

    companion object {
        private const val PLANS_REFRESH_MS = 60_000L
    }
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: com.momokotle.agent.data.remote.LaravelNotificationRemoteDataSource
) : NotificationRepository {
    override fun observeNotifications(userId: String): Flow<List<AppNotification>> =
        pollingListFlow { notificationRemoteDataSource.fetchNotifications() }

    override suspend fun markAsRead(notificationId: String) {
        notificationRemoteDataSource.markAsRead(notificationId)
    }

    override suspend fun markAllAsRead(userId: String) {
        notificationRemoteDataSource.markAllAsRead()
    }
}

private fun <T> pollingFlow(fetch: suspend () -> T?): Flow<T?> = kotlinx.coroutines.flow.flow {
    while (true) {
        emit(runCatching { fetch() }.getOrNull())
        kotlinx.coroutines.delay(POLLING_INTERVAL_MS)
    }
}

private fun <T> pollingListFlow(fetch: suspend () -> List<T>): Flow<List<T>> =
    kotlinx.coroutines.flow.flow {
        while (true) {
            emit(runCatching { fetch() }.getOrDefault(emptyList()))
            kotlinx.coroutines.delay(POLLING_INTERVAL_MS)
        }
    }

private const val POLLING_INTERVAL_MS = 60_000L
