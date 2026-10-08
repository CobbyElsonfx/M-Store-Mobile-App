package com.momokotle.agent.presentation.dashboard;

import android.content.Context;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.repository.SavedContactRepository;
import com.momokotle.agent.domain.repository.TransactionRepository;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.models.CustomFlow;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00b0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001BY\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u0016J\u0006\u0010+\u001a\u00020,J\u000e\u0010-\u001a\u00020,2\u0006\u0010.\u001a\u00020/J\u0006\u00100\u001a\u00020,J\u0006\u00101\u001a\u00020,J\u0006\u00102\u001a\u00020,J\u001e\u00103\u001a\u00020,2\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u000207H\u0082@\u00a2\u0006\u0002\u00108J\u000e\u00109\u001a\u00020,2\u0006\u0010:\u001a\u000205J\u000e\u0010;\u001a\u00020,2\u0006\u0010<\u001a\u00020=J\u000e\u0010>\u001a\u00020,2\u0006\u0010:\u001a\u000205J\u000e\u0010?\u001a\u00020,2\u0006\u0010@\u001a\u00020AJ\u000e\u0010B\u001a\u00020,2\u0006\u0010:\u001a\u000205J\u000e\u0010C\u001a\u00020,2\u0006\u0010<\u001a\u00020=J\u000e\u0010D\u001a\u00020,2\u0006\u0010:\u001a\u000205J\u000e\u0010E\u001a\u00020,2\u0006\u0010@\u001a\u00020AJ\u0006\u0010F\u001a\u00020,J\u0006\u0010G\u001a\u00020,J\u0006\u0010H\u001a\u00020,J\u0010\u0010I\u001a\u00020,2\u0006\u0010J\u001a\u000205H\u0002J\u000e\u0010K\u001a\u00020,2\u0006\u0010.\u001a\u00020/J\u0006\u0010L\u001a\u00020,J\u0006\u0010M\u001a\u00020,J\u0006\u0010N\u001a\u00020,J\u0006\u0010O\u001a\u00020,J\u0018\u0010P\u001a\u00020,2\u0006\u00106\u001a\u0002072\u0006\u0010Q\u001a\u000205H\u0002J\u0006\u0010R\u001a\u00020,J\u0006\u0010S\u001a\u00020,J\u0018\u0010T\u001a\u00020,2\u0006\u00106\u001a\u0002072\u0006\u0010U\u001a\u00020VH\u0002J\u0006\u0010W\u001a\u00020,J\u000e\u0010X\u001a\u0002052\u0006\u00106\u001a\u000207R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00190\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001d\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0!0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001fR\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u0019\u0010&\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\'0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001fR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006Y"}, d2 = {"Lcom/momokotle/agent/presentation/dashboard/DashboardViewModel;", "Lcom/momokotle/agent/presentation/common/BaseUssdViewModel;", "context", "Landroid/content/Context;", "startUssdFlowUseCase", "Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;", "automationEngine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "accessibilityRepository", "Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "recordAttemptedTransactionUseCase", "Lcom/momokotle/agent/domain/usecase/RecordAttemptedTransactionUseCase;", "transactionRepository", "Lcom/momokotle/agent/domain/repository/TransactionRepository;", "customFlowRepository", "Lcom/momokotle/agent/domain/repository/CustomFlowRepository;", "savedContactRepository", "Lcom/momokotle/agent/domain/repository/SavedContactRepository;", "saveContactUseCase", "Lcom/momokotle/agent/domain/usecase/SaveContactUseCase;", "(Landroid/content/Context;Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;Lcom/momokotle/agent/ussd/UssdAutomationEngine;Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/AccessibilityRepository;Lcom/momokotle/agent/domain/usecase/RecordAttemptedTransactionUseCase;Lcom/momokotle/agent/domain/repository/TransactionRepository;Lcom/momokotle/agent/domain/repository/CustomFlowRepository;Lcom/momokotle/agent/domain/repository/SavedContactRepository;Lcom/momokotle/agent/domain/usecase/SaveContactUseCase;)V", "_contactUiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/dashboard/DashboardContactUiState;", "_formState", "Lcom/momokotle/agent/presentation/dashboard/DashboardFormState;", "contactUiState", "Lkotlinx/coroutines/flow/StateFlow;", "getContactUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "customFlows", "", "Lcom/momokotle/agent/models/CustomFlow;", "getCustomFlows", "formState", "getFormState", "lastTransaction", "Lcom/momokotle/agent/models/Transaction;", "getLastTransaction", "searchJob", "Lkotlinx/coroutines/Job;", "acceptPostTransactionSave", "", "applyPrefillContact", "contact", "Lcom/momokotle/agent/models/SavedContact;", "dismissPostTransactionSavePrompt", "dismissSaveDialog", "dismissSearchSuggestions", "maybePromptSaveContact", "phone", "", "type", "Lcom/momokotle/agent/models/TransactionType;", "(Ljava/lang/String;Lcom/momokotle/agent/models/TransactionType;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onAmountChange", "value", "onNetworkChange", "network", "Lcom/momokotle/agent/models/ContactNetwork;", "onPhoneChange", "onPortedNumberChange", "checked", "", "onSaveDialogNameChange", "onSaveDialogNetworkChange", "onSaveDialogPhoneChange", "onSaveDialogPortedChange", "openSaveContactDialog", "repeatLastTransaction", "saveContactFromDialog", "scheduleContactSearch", "query", "selectContact", "startBalance", "startCashIn", "startCashOut", "startCommission", "startDetailedFlow", "flowName", "startEvdJust4u", "startPayToMerchant", "startSimpleFlow", "flow", "Lcom/momokotle/agent/ussd/UssdFlow;", "startTransferAirtime", "typeLabel", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends com.momokotle.agent.presentation.common.BaseUssdViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.SaveContactUseCase saveContactUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.CustomFlow>> customFlows = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.dashboard.DashboardFormState> _formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.dashboard.DashboardFormState> formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.dashboard.DashboardContactUiState> _contactUiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.dashboard.DashboardContactUiState> contactUiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.Transaction> lastTransaction = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job searchJob;
    
    @javax.inject.Inject()
    public DashboardViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.StartUssdFlowUseCase startUssdFlowUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdAutomationEngine automationEngine, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AccessibilityRepository accessibilityRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.TransactionRepository transactionRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.CustomFlowRepository customFlowRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.SavedContactRepository savedContactRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.SaveContactUseCase saveContactUseCase) {
        super(null, null, null, null, null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.CustomFlow>> getCustomFlows() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.dashboard.DashboardFormState> getFormState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.dashboard.DashboardContactUiState> getContactUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.models.Transaction> getLastTransaction() {
        return null;
    }
    
    public final void onPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onAmountChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onPortedNumberChange(boolean checked) {
    }
    
    public final void onNetworkChange(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network) {
    }
    
    public final void selectContact(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SavedContact contact) {
    }
    
    public final void dismissSearchSuggestions() {
    }
    
    private final void scheduleContactSearch(java.lang.String query) {
    }
    
    public final void openSaveContactDialog() {
    }
    
    public final void dismissSaveDialog() {
    }
    
    public final void onSaveDialogNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onSaveDialogPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onSaveDialogNetworkChange(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ContactNetwork network) {
    }
    
    public final void onSaveDialogPortedChange(boolean checked) {
    }
    
    public final void saveContactFromDialog() {
    }
    
    public final void dismissPostTransactionSavePrompt() {
    }
    
    public final void acceptPostTransactionSave() {
    }
    
    public final void applyPrefillContact(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SavedContact contact) {
    }
    
    public final void repeatLastTransaction() {
    }
    
    public final void startCashIn() {
    }
    
    public final void startCashOut() {
    }
    
    public final void startPayToMerchant() {
    }
    
    public final void startTransferAirtime() {
    }
    
    public final void startBalance() {
    }
    
    public final void startCommission() {
    }
    
    public final void startEvdJust4u() {
    }
    
    private final void startDetailedFlow(com.momokotle.agent.models.TransactionType type, java.lang.String flowName) {
    }
    
    private final java.lang.Object maybePromptSaveContact(java.lang.String phone, com.momokotle.agent.models.TransactionType type, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void startSimpleFlow(com.momokotle.agent.models.TransactionType type, com.momokotle.agent.ussd.UssdFlow flow) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String typeLabel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.TransactionType type) {
        return null;
    }
}