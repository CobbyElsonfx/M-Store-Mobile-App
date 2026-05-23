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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0011J\u0006\u0010\u001c\u001a\u00020\u0011J\u000e\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u001fR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006 "}, d2 = {"Lcom/momokotle/agent/presentation/profit/ProfitTypeSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "profitRuleRepository", "Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;", "ruleType", "Lcom/momokotle/agent/models/ProfitRuleType;", "(Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;Lcom/momokotle/agent/models/ProfitRuleType;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/profit/ProfitTypeSettingsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addRule", "", "clearMessage", "onDefaultPercentChange", "value", "", "onDraftAmountChange", "onDraftProfitPercentChange", "removePendingRule", "amount", "", "saveDefaultPercent", "saveSettings", "setCalculationMode", "mode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "app_debug"})
public abstract class ProfitTypeSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.ProfitRuleRepository profitRuleRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitRuleType ruleType = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.profit.ProfitTypeSettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.profit.ProfitTypeSettingsUiState> uiState = null;
    
    public ProfitTypeSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.ProfitRuleRepository profitRuleRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitRuleType ruleType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.profit.ProfitTypeSettingsUiState> getUiState() {
        return null;
    }
    
    public final void setCalculationMode(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode mode) {
    }
    
    public final void onDefaultPercentChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void saveDefaultPercent() {
    }
    
    public final void onDraftAmountChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onDraftProfitPercentChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void addRule() {
    }
    
    public final void removePendingRule(double amount) {
    }
    
    public final void saveSettings() {
    }
    
    public final void clearMessage() {
    }
}