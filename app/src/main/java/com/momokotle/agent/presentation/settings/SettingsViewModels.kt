package com.momokotle.agent.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.accessibility.MomoAccessibilityService
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.models.AppSettings
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.utils.AccessibilityHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val accessibilityEnabled: Boolean = false
)

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val accessibilityFlow = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsDataStore.settings,
        accessibilityFlow
    ) { settings, accessibility ->
        SettingsUiState(settings = settings, accessibilityEnabled = accessibility)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    init {
        refreshAccessibility()
    }

    fun refreshAccessibility() {
        accessibilityFlow.value = AccessibilityHelper.isAccessibilityServiceEnabled(
            context,
            MomoAccessibilityService::class.java
        )
    }

    fun openAccessibilitySettings() {
        AccessibilityHelper.openAccessibilitySettings(context)
    }

    fun setVibrateOnTouch(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.updateVibrateOnTouch(enabled) }
    }

    fun setTransactionVerification(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.updateTransactionVerification(enabled) }
    }

    fun setSimSlot(purpose: SimPurpose, slot: SimSlot) {
        viewModelScope.launch {
            when (purpose) {
                SimPurpose.MTN_MOBILE_MONEY -> settingsDataStore.updateSimMtnMomo(slot)
                SimPurpose.TELECEL_CASH -> settingsDataStore.updateSimTelecelCash(slot)
                SimPurpose.AIRTELTIGO_CASH -> settingsDataStore.updateSimAirtelTigoCash(slot)
                SimPurpose.MTN_AIRTIME_TRANSFER -> settingsDataStore.updateSimMtnAirtimeTransfer(slot)
            }
        }
    }
}
