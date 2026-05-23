package com.momokotle.agent.presentation.profit;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
import com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase;
import com.momokotle.agent.models.ProfitCalculationMode;
import com.momokotle.agent.models.ProfitResult;
import com.momokotle.agent.models.ProfitRule;
import com.momokotle.agent.models.ProfitRuleType;
import com.momokotle.agent.utils.AmountFormatter;
import com.momokotle.agent.utils.PercentFormatter;
import com.momokotle.agent.utils.ProfitAmountMatcher;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B[\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\nH\u00c6\u0003J\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J_\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010%\u001a\u00020&H\u00d6\u0001J\t\u0010\'\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018\u00a8\u0006("}, d2 = {"Lcom/momokotle/agent/presentation/profit/ProfitTypeSettingsUiState;", "", "calculationMode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "defaultPercentInput", "", "savedRules", "", "Lcom/momokotle/agent/models/ProfitRule;", "draftRule", "Lcom/momokotle/agent/presentation/profit/DraftProfitRule;", "pendingRules", "message", "errorMessage", "(Lcom/momokotle/agent/models/ProfitCalculationMode;Ljava/lang/String;Ljava/util/List;Lcom/momokotle/agent/presentation/profit/DraftProfitRule;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "getCalculationMode", "()Lcom/momokotle/agent/models/ProfitCalculationMode;", "getDefaultPercentInput", "()Ljava/lang/String;", "getDraftRule", "()Lcom/momokotle/agent/presentation/profit/DraftProfitRule;", "getErrorMessage", "getMessage", "getPendingRules", "()Ljava/util/List;", "getSavedRules", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ProfitTypeSettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitCalculationMode calculationMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String defaultPercentInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.ProfitRule> savedRules = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.presentation.profit.DraftProfitRule draftRule = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.momokotle.agent.models.ProfitRule> pendingRules = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String message = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public ProfitTypeSettingsUiState(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode calculationMode, @org.jetbrains.annotations.NotNull()
    java.lang.String defaultPercentInput, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> savedRules, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.profit.DraftProfitRule draftRule, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> pendingRules, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode getCalculationMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDefaultPercentInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.ProfitRule> getSavedRules() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.profit.DraftProfitRule getDraftRule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.ProfitRule> getPendingRules() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public ProfitTypeSettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.ProfitRule> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.profit.DraftProfitRule component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.momokotle.agent.models.ProfitRule> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.profit.ProfitTypeSettingsUiState copy(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode calculationMode, @org.jetbrains.annotations.NotNull()
    java.lang.String defaultPercentInput, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> savedRules, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.profit.DraftProfitRule draftRule, @org.jetbrains.annotations.NotNull()
    java.util.List<com.momokotle.agent.models.ProfitRule> pendingRules, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
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