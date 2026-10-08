package com.momokotle.agent.presentation.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.utils.PermissionHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UssdFlowUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val automationStatus: AutomationStatus = AutomationStatus.IDLE,
    val accessibilityEnabled: Boolean = false,
    val callPermissionGranted: Boolean = false
)

open class BaseUssdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    protected val startUssdFlowUseCase: StartUssdFlowUseCase,
    protected val automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    private val accessibilityRepository: AccessibilityRepository
) : ViewModel() {

    protected val _uiState = MutableStateFlow(UssdFlowUiState())
    val uiState: StateFlow<UssdFlowUiState> = _uiState.asStateFlow()

    init {
        refreshPermissions()
        viewModelScope.launch {
            accessibilityRepository.accessibilityStatus.collect { enabled ->
                _uiState.update { it.copy(accessibilityEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            automationEngine.status.collect { status ->
                _uiState.update { it.copy(automationStatus = status) }
                when (status) {
                    AutomationStatus.WAITING_FOR_USER ->
                        _uiState.update {
                            it.copy(infoMessage = "Enter your PIN on the USSD screen to complete.")
                        }
                    AutomationStatus.SUCCESS ->
                        _uiState.update {
                            it.copy(infoMessage = "Steps completed. Confirm result on USSD screen.")
                        }
                    AutomationStatus.FAILED ->
                        _uiState.update {
                            it.copy(errorMessage = automationEngine.lastError.value ?: "Automation failed")
                        }
                    else -> Unit
                }
            }
        }
        viewModelScope.launch {
            settingsDataStore.settings.collect { /* reserved for speed tuning */ }
        }
    }

    fun refreshPermissions() {
        accessibilityRepository.refresh()
        _uiState.update {
            it.copy(callPermissionGranted = PermissionHelper.hasCallPermission(context))
        }
    }

    fun openAccessibilitySettings() {
        accessibilityRepository.openAccessibilitySettings()
    }

    protected fun launchUssdFlow(
        flow: UssdFlow,
        phoneNumber: String = "",
        amount: String = "",
        transactionId: Long? = null
    ) {
        refreshPermissions()
        val state = _uiState.value

        when {
            !state.accessibilityEnabled -> {
                _uiState.update {
                    it.copy(errorMessage = "Enable M Storepay in Accessibility settings first.")
                }
                return
            }
            !state.callPermissionGranted -> {
                _uiState.update {
                    it.copy(errorMessage = "Grant phone call permission to dial USSD.")
                }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            startUssdFlowUseCase(
                flow = flow,
                phoneNumber = phoneNumber,
                amount = amount,
                transactionId = transactionId
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            infoMessage = "Dialing ${flow.ussdCode} — automation in progress…"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Could not start USSD flow"
                        )
                    }
                }
        }
    }

    fun cancelAutomation() {
        automationEngine.cancel()
        _uiState.update { it.copy(infoMessage = "Automation cancelled") }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }
}
