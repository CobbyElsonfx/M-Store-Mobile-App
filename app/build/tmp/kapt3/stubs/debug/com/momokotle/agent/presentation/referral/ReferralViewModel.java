package com.momokotle.agent.presentation.referral;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.ReferralDataStore;
import com.momokotle.agent.models.ReferralInfo;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u000e"}, d2 = {"Lcom/momokotle/agent/presentation/referral/ReferralViewModel;", "Landroidx/lifecycle/ViewModel;", "referralDataStore", "Lcom/momokotle/agent/data/datastore/ReferralDataStore;", "context", "Landroid/content/Context;", "(Lcom/momokotle/agent/data/datastore/ReferralDataStore;Landroid/content/Context;)V", "referralInfo", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/models/ReferralInfo;", "getReferralInfo", "()Lkotlinx/coroutines/flow/StateFlow;", "shareReferralLink", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReferralViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.ReferralDataStore referralDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.ReferralInfo> referralInfo = null;
    
    @javax.inject.Inject()
    public ReferralViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.ReferralDataStore referralDataStore, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.ReferralInfo> getReferralInfo() {
        return null;
    }
    
    public final void shareReferralLink(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}