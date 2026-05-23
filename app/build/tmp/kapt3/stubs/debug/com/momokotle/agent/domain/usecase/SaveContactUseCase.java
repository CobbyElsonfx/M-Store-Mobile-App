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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J.\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086B\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/momokotle/agent/domain/usecase/SaveContactUseCase;", "", "savedContactRepository", "Lcom/momokotle/agent/domain/repository/SavedContactRepository;", "(Lcom/momokotle/agent/domain/repository/SavedContactRepository;)V", "invoke", "Lcom/momokotle/agent/domain/usecase/SaveContactResult;", "contactName", "", "phoneNumber", "network", "Lcom/momokotle/agent/models/ContactNetwork;", "isPorted", "", "(Ljava/lang/String;Ljava/lang/String;Lcom/momokotle/agent/models/ContactNetwork;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class SaveContactUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository = null;
    
    @javax.inject.Inject()
    public SaveContactUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    java.lang.String contactName, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network, boolean isPorted, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.domain.usecase.SaveContactResult> $completion) {
        return null;
    }
}