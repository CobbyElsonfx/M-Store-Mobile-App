package com.momokotle.agent.domain.usecase;

import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.ProfitRuleRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.models.ContactNetwork;
import com.momokotle.agent.models.SavedContact;
import com.momokotle.agent.models.ProfitCalculationMode;
import com.momokotle.agent.models.ProfitResult;
import com.momokotle.agent.models.ProfitRuleType;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.utils.PercentFormatter;
import com.momokotle.agent.utils.ProfitAmountMatcher;
import com.momokotle.agent.models.SimPurpose;
import com.momokotle.agent.models.SimSlot;
import com.momokotle.agent.models.Transaction;
import com.momokotle.agent.models.TransactionStatus;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdDialHelper;
import com.momokotle.agent.ussd.UssdFlow;
import com.momokotle.agent.ussd.UssdFlowContext;
import com.momokotle.agent.ussd.UssdFlows;
import com.momokotle.agent.utils.PhoneNumberFormatter;
import com.momokotle.agent.utils.TransactionVerificationAnalyzer;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ@\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0086B\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0014JD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0086B\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0018"}, d2 = {"Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;", "", "dialHelper", "Lcom/momokotle/agent/ussd/UssdDialHelper;", "engine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "(Lcom/momokotle/agent/ussd/UssdDialHelper;Lcom/momokotle/agent/ussd/UssdAutomationEngine;Lcom/momokotle/agent/data/datastore/SettingsDataStore;)V", "invoke", "Lkotlin/Result;", "", "type", "Lcom/momokotle/agent/models/TransactionType;", "phoneNumber", "", "amount", "transactionId", "", "invoke-yxL6bBk", "(Lcom/momokotle/agent/models/TransactionType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "flow", "Lcom/momokotle/agent/ussd/UssdFlow;", "(Lcom/momokotle/agent/ussd/UssdFlow;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class StartUssdFlowUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.ussd.UssdDialHelper dialHelper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.ussd.UssdAutomationEngine engine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    
    @javax.inject.Inject()
    public StartUssdFlowUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdDialHelper dialHelper, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdAutomationEngine engine, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore) {
        super();
    }
}