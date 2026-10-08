package com.momokotle.agent.presentation.settings;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.domain.usecase.LogoutUseCase;
import com.momokotle.agent.models.UserAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0013R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/momokotle/agent/presentation/settings/SettingsHomeViewModel;", "Landroidx/lifecycle/ViewModel;", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "logoutUseCase", "Lcom/momokotle/agent/domain/usecase/LogoutUseCase;", "context", "Landroid/content/Context;", "(Lcom/momokotle/agent/data/datastore/UserAccountDataStore;Lcom/momokotle/agent/domain/usecase/LogoutUseCase;Landroid/content/Context;)V", "_isLoggingOut", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "account", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/models/UserAccount;", "getAccount", "()Lkotlinx/coroutines/flow/StateFlow;", "isLoggingOut", "logout", "", "shareApp", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsHomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.LogoutUseCase logoutUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> account = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoggingOut = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoggingOut = null;
    
    @javax.inject.Inject()
    public SettingsHomeViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.LogoutUseCase logoutUseCase, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> getAccount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoggingOut() {
        return null;
    }
    
    public final void logout() {
    }
    
    public final void shareApp() {
    }
}