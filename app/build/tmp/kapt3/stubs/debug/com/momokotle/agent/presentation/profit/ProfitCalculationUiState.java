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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003JI\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011\u00a8\u0006\""}, d2 = {"Lcom/momokotle/agent/presentation/profit/ProfitCalculationUiState;", "", "cashInAmount", "", "cashOutAmount", "cashInResult", "Lcom/momokotle/agent/models/ProfitResult;", "cashOutResult", "cashInMode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "cashOutMode", "(Ljava/lang/String;Ljava/lang/String;Lcom/momokotle/agent/models/ProfitResult;Lcom/momokotle/agent/models/ProfitResult;Lcom/momokotle/agent/models/ProfitCalculationMode;Lcom/momokotle/agent/models/ProfitCalculationMode;)V", "getCashInAmount", "()Ljava/lang/String;", "getCashInMode", "()Lcom/momokotle/agent/models/ProfitCalculationMode;", "getCashInResult", "()Lcom/momokotle/agent/models/ProfitResult;", "getCashOutAmount", "getCashOutMode", "getCashOutResult", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ProfitCalculationUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cashInAmount = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cashOutAmount = null;
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.ProfitResult cashInResult = null;
    @org.jetbrains.annotations.Nullable()
    private final com.momokotle.agent.models.ProfitResult cashOutResult = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitCalculationMode cashInMode = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitCalculationMode cashOutMode = null;
    
    public ProfitCalculationUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String cashInAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String cashOutAmount, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.ProfitResult cashInResult, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.ProfitResult cashOutResult, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashInMode, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashOutMode) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCashInAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCashOutAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.ProfitResult getCashInResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.ProfitResult getCashOutResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode getCashInMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode getCashOutMode() {
        return null;
    }
    
    public ProfitCalculationUiState() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.ProfitResult component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.momokotle.agent.models.ProfitResult component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.presentation.profit.ProfitCalculationUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String cashInAmount, @org.jetbrains.annotations.NotNull()
    java.lang.String cashOutAmount, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.ProfitResult cashInResult, @org.jetbrains.annotations.Nullable()
    com.momokotle.agent.models.ProfitResult cashOutResult, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashInMode, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashOutMode) {
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