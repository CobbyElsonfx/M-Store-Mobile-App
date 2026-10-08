package com.momokotle.agent.presentation.custommenu;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.CustomMenuType;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.presentation.common.BaseUssdViewModel;
import com.momokotle.agent.ussd.CustomFlowExecutor;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0019\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BQ\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\rJ\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\nH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\nH\u00c6\u0003JU\u0010 \u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010!\u001a\u00020\u00072\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\f\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017\u00a8\u0006&"}, d2 = {"Lcom/momokotle/agent/presentation/custommenu/CustomMenuExecutionUiState;", "", "flow", "Lcom/momokotle/agent/models/CustomFlow;", "menuType", "Lcom/momokotle/agent/models/CustomMenuType;", "requiresPhone", "", "requiresAmount", "phoneNumber", "", "amount", "loadError", "(Lcom/momokotle/agent/models/CustomFlow;Lcom/momokotle/agent/models/CustomMenuType;ZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAmount", "()Ljava/lang/String;", "getFlow", "()Lcom/momokotle/agent/models/CustomFlow;", "getLoadError", "getMenuType", "()Lcom/momokotle/agent/models/CustomMenuType;", "getPhoneNumber", "getRequiresAmount", "()Z", "getRequiresPhone", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class CustomMenuExecutionUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.CustomFlow flow = null;
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.CustomMenuType menuType = null;
    private final boolean requiresPhone = false;
    private final boolean requiresAmount = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String amount = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String loadError = null;
    
    public CustomMenuExecutionUiState(@org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.CustomMenuType menuType, boolean requiresPhone, boolean requiresAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, @org.jetbrains.annotations.Nullable()
    java.lang.String loadError) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.CustomFlow getFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.CustomMenuType getMenuType() {
        return null;
    }
    
    public final boolean getRequiresPhone() {
        return false;
    }
    
    public final boolean getRequiresAmount() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLoadError() {
        return null;
    }
    
    public CustomMenuExecutionUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.CustomFlow component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.CustomMenuType component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.custommenu.CustomMenuExecutionUiState copy(@org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.CustomFlow flow, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.CustomMenuType menuType, boolean requiresPhone, boolean requiresAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, @org.jetbrains.annotations.Nullable()
    java.lang.String loadError) {
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