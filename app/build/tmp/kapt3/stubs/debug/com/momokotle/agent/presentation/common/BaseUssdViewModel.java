package com.momokotle.agent.presentation.common;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase;
import com.momokotle.agent.models.AutomationStatus;
import com.momokotle.agent.ussd.UssdAutomationEngine;
import com.momokotle.agent.ussd.UssdFlow;
import com.momokotle.agent.utils.PermissionHelper;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u001bJ5\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020!2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u0004\u00a2\u0006\u0002\u0010%J\u0006\u0010&\u001a\u00020\u001bJ\u0006\u0010\'\u001a\u00020\u001bR\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0007X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006("}, d2 = {"Lcom/momokotle/agent/presentation/common/BaseUssdViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "startUssdFlowUseCase", "Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;", "automationEngine", "Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "accessibilityRepository", "Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "(Landroid/content/Context;Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;Lcom/momokotle/agent/ussd/UssdAutomationEngine;Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/AccessibilityRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/momokotle/agent/presentation/common/UssdFlowUiState;", "get_uiState", "()Lkotlinx/coroutines/flow/MutableStateFlow;", "getAutomationEngine", "()Lcom/momokotle/agent/ussd/UssdAutomationEngine;", "getStartUssdFlowUseCase", "()Lcom/momokotle/agent/domain/usecase/StartUssdFlowUseCase;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "cancelAutomation", "", "clearMessages", "launchUssdFlow", "flow", "Lcom/momokotle/agent/ussd/UssdFlow;", "phoneNumber", "", "amount", "transactionId", "", "(Lcom/momokotle/agent/ussd/UssdFlow;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;)V", "openAccessibilitySettings", "refreshPermissions", "app_debug"})
public class BaseUssdViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.usecase.StartUssdFlowUseCase startUssdFlowUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.ussd.UssdAutomationEngine automationEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.AccessibilityRepository accessibilityRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.common.UssdFlowUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.common.UssdFlowUiState> uiState = null;
    
    @javax.inject.Inject()
    public BaseUssdViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.usecase.StartUssdFlowUseCase startUssdFlowUseCase, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdAutomationEngine automationEngine, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AccessibilityRepository accessibilityRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.momokotle.agent.domain.usecase.StartUssdFlowUseCase getStartUssdFlowUseCase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.momokotle.agent.ussd.UssdAutomationEngine getAutomationEngine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final kotlinx.coroutines.flow.MutableStateFlow<com.momokotle.agent.presentation.common.UssdFlowUiState> get_uiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.common.UssdFlowUiState> getUiState() {
        return null;
    }
    
    public final void refreshPermissions() {
    }
    
    public final void openAccessibilitySettings() {
    }
    
    protected final void launchUssdFlow(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ussd.UssdFlow flow, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, @org.jetbrains.annotations.Nullable()
    java.lang.Long transactionId) {
    }
    
    public final void cancelAutomation() {
    }
    
    public final void clearMessages() {
    }
}