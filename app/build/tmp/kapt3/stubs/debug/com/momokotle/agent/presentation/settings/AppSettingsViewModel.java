package com.momokotle.agent.presentation.settings;

import androidx.lifecycle.ViewModel;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.domain.repository.AccessibilityRepository;
import com.momokotle.agent.models.AppSettings;
import com.momokotle.agent.models.SimPurpose;
import com.momokotle.agent.models.SimSlot;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0016\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2 = {"Lcom/momokotle/agent/presentation/settings/AppSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "accessibilityRepository", "Lcom/momokotle/agent/domain/repository/AccessibilityRepository;", "(Lcom/momokotle/agent/data/datastore/SettingsDataStore;Lcom/momokotle/agent/domain/repository/AccessibilityRepository;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/presentation/settings/SettingsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "openAccessibilitySettings", "", "refreshAccessibility", "setSimSlot", "purpose", "Lcom/momokotle/agent/models/SimPurpose;", "slot", "Lcom/momokotle/agent/models/SimSlot;", "setTransactionVerification", "enabled", "", "setVibrateOnTouch", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AppSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.domain.repository.AccessibilityRepository accessibilityRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.settings.SettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public AppSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.domain.repository.AccessibilityRepository accessibilityRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.settings.SettingsUiState> getUiState() {
        return null;
    }
    
    public final void refreshAccessibility() {
    }
    
    public final void openAccessibilitySettings() {
    }
    
    public final void setVibrateOnTouch(boolean enabled) {
    }
    
    public final void setTransactionVerification(boolean enabled) {
    }
    
    public final void setSimSlot(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimPurpose purpose, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot) {
    }
}