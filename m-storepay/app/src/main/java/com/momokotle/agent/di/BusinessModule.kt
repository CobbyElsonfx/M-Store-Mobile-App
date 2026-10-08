package com.momokotle.agent.di

import com.momokotle.agent.domain.repository.BusinessSettingsRepository
import com.momokotle.agent.domain.repository.NotificationRepository
import com.momokotle.agent.domain.repository.ReferralRepository
import com.momokotle.agent.domain.repository.SubscriptionRepository
import com.momokotle.agent.domain.repository.WalletRepository
import com.momokotle.agent.repository.BusinessSettingsRepositoryImpl
import com.momokotle.agent.repository.NotificationRepositoryImpl
import com.momokotle.agent.repository.ReferralRepositoryImpl
import com.momokotle.agent.repository.SubscriptionRepositoryImpl
import com.momokotle.agent.repository.WalletRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BusinessModule {

    @Binds
    @Singleton
    abstract fun bindBusinessSettingsRepository(
        impl: BusinessSettingsRepositoryImpl
    ): BusinessSettingsRepository

    @Binds
    @Singleton
    abstract fun bindReferralRepository(impl: ReferralRepositoryImpl): ReferralRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(impl: WalletRepositoryImpl): WalletRepository

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(
        impl: SubscriptionRepositoryImpl
    ): SubscriptionRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}
