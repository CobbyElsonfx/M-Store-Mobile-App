package com.momokotle.agent.presentation.custommenu;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.domain.repository.CustomFlowRepository;
import com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.models.CustomFlow;
import com.momokotle.agent.models.TransactionType;
import com.momokotle.agent.presentation.common.BaseUssdViewModel;
import com.momokotle.agent.ussd.CustomFlowMapper;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.utils.AmountFormatter;
import com.momokotle.agent.utils.PhoneNumberFormatter;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B9\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u0017J\u000e\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u000e\u0010(\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u000e\u0010)\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010*\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010+\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010,\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010-\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010.\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&J\u000e\u0010/\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u0017J\u0006\u00100\u001a\u00020\u001eJ\u0006\u00101\u001a\u00020\u001eR\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00130\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0019R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/momokotle/agent/presentation/custommenu/CustomFlowViewModel;", "Lcom/momokotle/agent/presentation/common/BaseUssdViewModel;", "context", "Landroid/content/Context;", "startUssdFlowUseCase", "Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;", "automationEngine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "customFlowRepository", "Lcom/momokotle/agent/domain/repository/CustomFlowRepository;", "recordAttemptedTransactionUseCase", "Lcom/momokotle/agent/domain/usecase/RecordAttemptedTransactionUseCase;", "(Landroid/content/Context;Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;Lcom/momokotle/agent/ussd/UssdAutomationEngine;Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/CustomFlowRepository;Lcom/momokotle/agent/domain/usecase/RecordAttemptedTransactionUseCase;)V", "_formState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/custommenu/CustomFlowFormState;", "_isEditing", "", "customFlows", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/momokotle/agent/models/CustomFlow;", "getCustomFlows", "()Lkotlinx/coroutines/flow/StateFlow;", "formState", "getFormState", "isEditing", "cancelEdit", "", "deleteFlow", "id", "", "editFlow", "flow", "onAmountChange", "v", "", "onDynamicAmountChange", "onDynamicPhoneChange", "onNameChange", "onPhoneChange", "onStep1Change", "onStep2Change", "onStep3Change", "onUssdCodeChange", "runFlow", "saveFlow", "startNewFlow", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CustomFlowViewModel extends com.momokotle.agent.presentation.common.BaseUssdViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.CustomFlowRepository customFlowRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.CustomFlow>> customFlows = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.custommenu.CustomFlowFormState> _formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.custommenu.CustomFlowFormState> formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isEditing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isEditing = null;
    
    @javax.inject.Inject()
    public CustomFlowViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.StartUssdFlowUseCase startUssdFlowUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdAutomationEngine automationEngine, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.CustomFlowRepository customFlowRepository, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.RecordAttemptedTransactionUseCase recordAttemptedTransactionUseCase) {
        super(null, null, null, null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.momokotle.agent.models.CustomFlow>> getCustomFlows() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.custommenu.CustomFlowFormState> getFormState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isEditing() {
        return null;
    }
    
    public final void onNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onUssdCodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onStep1Change(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onStep2Change(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onStep3Change(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onAmountChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onDynamicPhoneChange(boolean v) {
    }
    
    public final void onDynamicAmountChange(boolean v) {
    }
    
    public final void startNewFlow() {
    }
    
    public final void editFlow(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow) {
    }
    
    public final void cancelEdit() {
    }
    
    public final void saveFlow() {
    }
    
    public final void deleteFlow(long id) {
    }
    
    public final void runFlow(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.CustomFlow flow) {
    }
}