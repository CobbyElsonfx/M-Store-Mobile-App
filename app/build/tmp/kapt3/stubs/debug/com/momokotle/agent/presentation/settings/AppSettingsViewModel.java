package com.momokotle.agent.presentation.settings;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.momokotle.agent.accessibility.MomoAccessibilityService;
import com.momokotle.agent.data.datastore.SettingsDataStore;
import com.momokotle.agent.models.AppSettings;
import com.momokotle.agent.models.SimPurpose;
import com.momokotle.agent.models.SimSlot;
import com.momokotle.agent.utils.AccessibilityHelper;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0010J\u0016\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\tJ\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\tR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/momokotle/agent/presentation/settings/AppSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "settingsDataStore", "Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "(Landroid/content/Context;Lcom/momokotle/agent/data/datastore/SettingsDataStore;)V", "accessibilityFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/momokotle/agent/presentation/settings/SettingsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "openAccessibilitySettings", "", "refreshAccessibility", "setSimSlot", "purpose", "Lcom/momokotle/agent/models/SimPurpose;", "slot", "Lcom/momokotle/agent/models/SimSlot;", "setTransactionVerification", "enabled", "setVibrateOnTouch", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AppSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> accessibilityFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.momokotle.agent.presentation.settings.SettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public AppSettingsViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.SettingsDataStore settingsDataStore) {
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