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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/momokotle/agent/presentation/profit/CashOutSettingsViewModel;", "Lcom/momokotle/agent/presentation/profit/ProfitTypeSettingsViewModel;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "profitRuleRepository", "Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;", "(Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/ProfitRuleRepository;)V", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CashOutSettingsViewModel extends com.momokotle.agent.presentation.profit.ProfitTypeSettingsViewModel {
    
    @javax.inject.Inject()
    public CashOutSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.ProfitRuleRepository profitRuleRepository) {
        super(null, null, null);
    }
}