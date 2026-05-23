package com.momokotle.agent.di;

import android.content.Context;
import androidx.room.Room;
import com.momokotle.agent.database.AppDatabase;
import com.momokotle.agent.database.dao.CustomFlowDao;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.database.dao.ProfitRuleDao;
import com.momokotle.agent.database.dao.SavedContactDao;
import com.momokotle.agent.database.dao.TransactionDao;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.repository.CustomFlowRepositoryImpl;
import com.momokotle.agent.repository.FormSubmissionRepositoryImpl;
import com.momokotle.agent.repository.StoreRepositoryImpl;
import com.momokotle.agent.repository.UserIdProviderImpl;
import com.momokotle.agent.repository.ProfitRuleRepositoryImpl;
import com.momokotle.agent.repository.SavedContactRepositoryImpl;
import com.momokotle.agent.repository.TransactionRepositoryImpl;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdDialHelper;
import com.momokotle.agent.utils.AutomationLogger;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\'J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0012H\'J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0015H\'J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0018H\'\u00a8\u0006\u0019"}, d2 = {"Lcom/momokotle/agent/di/RepositoryModule;", "", "()V", "bindCustomFlowRepository", "Lcom/momokotle/agent/domain/repository/CustomFlowRepository;", "impl", "Lcom/momokotle/agent/repository/CustomFlowRepositoryImpl;", "bindFormSubmissionRepository", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "Lcom/momokotle/agent/repository/FormSubmissionRepositoryImpl;", "bindProfitRuleRepository", "Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;", "Lcom/momokotle/agent/repository/ProfitRuleRepositoryImpl;", "bindSavedContactRepository", "Lcom/momokotle/agent/domain/repository/SavedContactRepository;", "Lcom/momokotle/agent/repository/SavedContactRepositoryImpl;", "bindStoreRepository", "Lcom/momokotle/agent/domain/repository/StoreRepository;", "Lcom/momokotle/agent/repository/StoreRepositoryImpl;", "bindTransactionRepository", "Lcom/momokotle/agent/domain/repository/TransactionRepository;", "Lcom/momokotle/agent/repository/TransactionRepositoryImpl;", "bindUserIdProvider", "Lcom/momokotle/agent/domain/repository/UserIdProvider;", "Lcom/momokotle/agent/repository/UserIdProviderImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.TransactionRepository bindTransactionRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.TransactionRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.SavedContactRepository bindSavedContactRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.SavedContactRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.ProfitRuleRepository bindProfitRuleRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.ProfitRuleRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.CustomFlowRepository bindCustomFlowRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.CustomFlowRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.StoreRepository bindStoreRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.StoreRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.FormSubmissionRepository bindFormSubmissionRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.FormSubmissionRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.momokotle.agent.domain.repository.UserIdProvider bindUserIdProvider(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.UserIdProviderImpl impl);
}