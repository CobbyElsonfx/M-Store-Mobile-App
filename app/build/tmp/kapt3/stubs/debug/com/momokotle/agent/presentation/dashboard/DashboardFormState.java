package com.momokotle.agent.presentation.dashboard;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.SaveContactResult;
import com.momokotle.agent.domain.usecase.SaveContactUseCase;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.models.ContactNetwork;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.presentation.common.BaseUssdViewModel;
import com.momokotle.agent.presentation.transactions.TransactionsViewModel;
import com.momokotle.agent.ui.components.SaveContactFormState;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdFlows;
import com.momokotle.agent.utils.AmountFormatter;
import com.momokotle.agent.utils.PhoneNumberFormatter;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\bH\u00c6\u0003J1\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0015\u001a\u00020\u00062\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b\u00a8\u0006\u001a"}, d2 = {"Lcom/momokotle/agent/presentation/dashboard/DashboardFormState;", "", "phoneNumber", "", "amount", "isPortedNumber", "", "network", "Lcom/momokotle/agent/models/ContactNetwork;", "(Ljava/lang/String;Ljava/lang/String;ZLcom/momokotle/agent/models/ContactNetwork;)V", "getAmount", "()Ljava/lang/String;", "()Z", "getNetwork", "()Lcom/momokotle/agent/models/ContactNetwork;", "getPhoneNumber", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class DashboardFormState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String amount = null;
    private final boolean isPortedNumber = false;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ContactNetwork network = null;
    
    public DashboardFormState(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, boolean isPortedNumber, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAmount() {
        return null;
    }
    
    public final boolean isPortedNumber() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ContactNetwork getNetwork() {
        return null;
    }
    
    public DashboardFormState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ContactNetwork component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.dashboard.DashboardFormState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, boolean isPortedNumber, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}