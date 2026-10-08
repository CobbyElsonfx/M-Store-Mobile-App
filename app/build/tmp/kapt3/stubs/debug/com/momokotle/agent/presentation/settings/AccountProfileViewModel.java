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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/momokotle/agent/presentation/settings/AccountProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "(Lcom/momokotle/agent/data/datastore/UserAccountDataStore;)V", "account", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/models/UserAccount;", "getAccount", "()Lkotlinx/coroutines/flow/StateFlow;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AccountProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> account = null;
    
    @javax.inject.Inject()
    public AccountProfileViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.UserAccount> getAccount() {
        return null;
    }
}