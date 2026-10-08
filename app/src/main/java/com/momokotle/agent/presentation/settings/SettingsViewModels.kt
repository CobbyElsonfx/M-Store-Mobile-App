package com.momokotle.agent.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.models.AppSettings
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val settingsDataStore: SettingsDataStore,
    private val accessibilityRepository: AccessibilityRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsDataStore.settings,
        accessibilityRepository.accessibilityStatus
    ) { settings, accessibility ->
        SettingsUiState(settings = settings, accessibilityEnabled = accessibility)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun refreshAccessibility() {
        accessibilityRepository.refresh()
    }

    fun openAccessibilitySettings() {
        accessibilityRepository.openAccessibilitySettings()
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
