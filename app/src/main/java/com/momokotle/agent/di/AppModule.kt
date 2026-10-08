package com.momokotle.agent.di

import android.content.Context
import androidx.room.Room
import com.momokotle.agent.database.AppDatabase
import com.momokotle.agent.database.dao.CustomFlowDao
import com.momokotle.agent.database.dao.FormSubmissionDao
import com.momokotle.agent.database.dao.StoreOrderDao
import com.momokotle.agent.database.dao.ProfitRuleDao
import com.momokotle.agent.database.dao.SavedContactDao
import com.momokotle.agent.database.dao.TransactionDao
import com.momokotle.agent.database.dao.StoreProductDao
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.domain.repository.CustomFlowRepository
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.repository.UserIdProvider
import com.momokotle.agent.domain.repository.ProfitRuleRepository
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.repository.AccessibilityRepositoryImpl
import com.momokotle.agent.repository.AuthRepositoryImpl
import com.momokotle.agent.repository.FormSubmissionRepositoryImpl
import com.momokotle.agent.repository.OrderRepositoryImpl
import com.momokotle.agent.repository.ProductRepositoryImpl
import com.momokotle.agent.repository.CustomFlowRepositoryImpl
import com.momokotle.agent.repository.StoreRepositoryImpl
import com.momokotle.agent.repository.UserIdProviderImpl
import com.momokotle.agent.repository.ProfitRuleRepositoryImpl
import com.momokotle.agent.repository.SavedContactRepositoryImpl
import com.momokotle.agent.repository.TransactionRepositoryImpl
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdDialHelper
import com.momokotle.agent.utils.AutomationLogger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "momo_kotle.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideSavedContactDao(db: AppDatabase): SavedContactDao = db.savedContactDao()

    @Provides
    fun provideProfitRuleDao(db: AppDatabase): ProfitRuleDao = db.profitRuleDao()

    @Provides
    fun provideCustomFlowDao(db: AppDatabase): CustomFlowDao = db.customFlowDao()

    @Provides
    fun provideStoreOrderDao(db: AppDatabase): StoreOrderDao = db.storeOrderDao()

    @Provides
    fun provideFormSubmissionDao(db: AppDatabase): FormSubmissionDao = db.formSubmissionDao()

    @Provides
    fun provideStoreProductDao(db: AppDatabase): StoreProductDao = db.storeProductDao()

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideUssdDialHelper(@ApplicationContext context: Context): UssdDialHelper =
        UssdDialHelper(context)

    @Provides
    @Singleton
    fun provideUssdAutomationEngine(logger: AutomationLogger): UssdAutomationEngine =
        UssdAutomationEngine(logger)

    @Provides
    @Singleton
    fun provideAccessibilityRepository(
        impl: AccessibilityRepositoryImpl
    ): AccessibilityRepository = impl
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindSavedContactRepository(impl: SavedContactRepositoryImpl): SavedContactRepository

    @Binds
    @Singleton
    abstract fun bindProfitRuleRepository(impl: ProfitRuleRepositoryImpl): ProfitRuleRepository

    @Binds
    @Singleton
    abstract fun bindCustomFlowRepository(impl: CustomFlowRepositoryImpl): CustomFlowRepository

    @Binds
    @Singleton
    abstract fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository

    @Binds
    @Singleton
    abstract fun bindFormSubmissionRepository(impl: FormSubmissionRepositoryImpl): FormSubmissionRepository

    @Binds
    @Singleton
    abstract fun bindUserIdProvider(impl: UserIdProviderImpl): UserIdProvider
}
