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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0015"}, d2 = {"Lcom/momokotle/agent/presentation/profit/ProfitCalculationViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "calculateVendorProfitUseCase", "Lcom/momokotle/agent/domain/usecase/CalculateVendorProfitUseCase;", "(Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/usecase/CalculateVendorProfitUseCase;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/profit/ProfitCalculationUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calculateCashIn", "", "calculateCashOut", "onCashInAmountChange", "value", "", "onCashOutAmountChange", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProfitCalculationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase calculateVendorProfitUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.profit.ProfitCalculationUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.profit.ProfitCalculationUiState> uiState = null;
    
    @javax.inject.Inject()
    public ProfitCalculationViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase calculateVendorProfitUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.profit.ProfitCalculationUiState> getUiState() {
        return null;
    }
    
    public final void onCashInAmountChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onCashOutAmountChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void calculateCashIn() {
    }
    
    public final void calculateCashOut() {
    }
}