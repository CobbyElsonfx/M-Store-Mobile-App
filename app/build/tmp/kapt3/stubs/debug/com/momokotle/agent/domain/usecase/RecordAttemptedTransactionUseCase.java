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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ4\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u000eH\u0086B\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/momokotle/agent/domain/usecase/RecordAttemptedTransactionUseCase;", "", "transactionRepository", "Lcom/momokotle/agent/domain/repository/TransactionRepository;", "calculateVendorProfitUseCase", "Lcom/momokotle/agent/domain/usecase/CalculateVendorProfitUseCase;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "(Lcom/momokotle/agent/domain/repository/TransactionRepository;Lcom/momokotle/agent/domain/usecase/CalculateVendorProfitUseCase;Lcom/momokotle/agent/data/datastore/SettingsDataStore;)V", "invoke", "", "type", "Lcom/momokotle/agent/models/TransactionType;", "flowName", "", "amount", "", "phoneNumber", "(Lcom/momokotle/agent/models/TransactionType;Ljava/lang/String;DLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RecordAttemptedTransactionUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.TransactionRepository transactionRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase calculateVendorProfitUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    
    @javax.inject.Inject()
    public RecordAttemptedTransactionUseCase(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.TransactionRepository transactionRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.CalculateVendorProfitUseCase calculateVendorProfitUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.TransactionType type, @org.jetbrains.annotations.NotNull()
    java.lang.String flowName, double amount, @org.jetbrains.annotations.Nullable()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
}