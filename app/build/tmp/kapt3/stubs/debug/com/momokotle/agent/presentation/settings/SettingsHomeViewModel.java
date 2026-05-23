package com.momokotle.agent.presentation.settings;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.models.UserAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\f\u001a\u00020\rR\u0019\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/momokotle/agent/presentation/settings/SettingsHomeViewModel;", "Landroidx/lifecycle/ViewModel;", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "context", "Landroid/content/Context;", "(Lcom/momokotle/agent/data/datastore/UserAccountDataStore;Landroid/content/Context;)V", "account", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/models/UserAccount;", "getAccount", "()Lkotlinx/coroutines/flow/StateFlow;", "shareApp", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsHomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> account = null;
    
    @javax.inject.Inject()
    public SettingsHomeViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> getAccount() {
        return null;
    }
    
    public final void shareApp() {
    }
}