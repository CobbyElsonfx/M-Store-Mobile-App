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
import com.momokotle.agent.database.dao.StoreProductDao;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.repository.AuthRepository;
import com.momokotle.agent.domain.repository.OrderRepository;
import com.momokotle.agent.domain.repository.ProductRepository;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.repository.AccessibilityRepositoryImpl;
import com.momokotle.agent.repository.AuthRepositoryImpl;
import com.momokotle.agent.repository.FormSubmissionRepositoryImpl;
import com.momokotle.agent.repository.OrderRepositoryImpl;
import com.momokotle.agent.repository.ProductRepositoryImpl;
import com.momokotle.agent.repository.CustomFlowRepositoryImpl;
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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\r\u001a\u00020\f2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007J\u0012\u0010 \u001a\u00020!2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007\u00a8\u0006\""}, d2 = {"Lcom/momokotle/agent/di/AppModule;", "", "()V", "provideAccessibilityRepository", "Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "impl", "Lcom/momokotle/agent/repository/AccessibilityRepositoryImpl;", "provideApplicationScope", "Lkotlinx/coroutines/CoroutineScope;", "provideCustomFlowDao", "Lcom/momokotle/agent/database/dao/CustomFlowDao;", "db", "Lcom/momokotle/agent/database/AppDatabase;", "provideDatabase", "context", "Landroid/content/Context;", "provideFormSubmissionDao", "Lcom/momokotle/agent/database/dao/FormSubmissionDao;", "provideProfitRuleDao", "Lcom/momokotle/agent/database/dao/ProfitRuleDao;", "provideSavedContactDao", "Lcom/momokotle/agent/database/dao/SavedContactDao;", "provideStoreOrderDao", "Lcom/momokotle/agent/database/dao/StoreOrderDao;", "provideStoreProductDao", "Lcom/momokotle/agent/database/dao/StoreProductDao;", "provideTransactionDao", "Lcom/momokotle/agent/database/dao/TransactionDao;", "provideUssdAutomationEngine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "logger", "Lcom/momokotle/agent/utils/AutomationLogger;", "provideUssdDialHelper", "Lcom/momokotle/agent/ussd/UssdDialHelper;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.AppDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.TransactionDao provideTransactionDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.SavedContactDao provideSavedContactDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.ProfitRuleDao provideProfitRuleDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.CustomFlowDao provideCustomFlowDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.StoreOrderDao provideStoreOrderDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.FormSubmissionDao provideFormSubmissionDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.database.dao.StoreProductDao provideStoreProductDao(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @ApplicationScope()
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.CoroutineScope provideApplicationScope() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ussd.UssdDialHelper provideUssdDialHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.ussd.UssdAutomationEngine provideUssdAutomationEngine(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.AutomationLogger logger) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.domain.repository.AccessibilityRepository provideAccessibilityRepository(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.repository.AccessibilityRepositoryImpl impl) {
        return null;
    }
}