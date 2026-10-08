package com.momokotle.agent.presentation.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.usecase.CheckUssdAccessUseCase
import com.momokotle.agent.domain.usecase.StartUssdFlowUseCase
import com.momokotle.agent.domain.ussd.UssdAccessResult
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.utils.PermissionHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val callPermissionGranted: Boolean = false,
    val subscriptionAccess: UssdAccessResult = UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN
)

open class BaseUssdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    protected val startUssdFlowUseCase: StartUssdFlowUseCase,
    protected val automationEngine: UssdAutomationEngine,
    settingsDataStore: SettingsDataStore,
    private val accessibilityRepository: AccessibilityRepository,
    private val checkUssdAccessUseCase: CheckUssdAccessUseCase,
    private val cashOutSmsWatcher: com.momokotle.agent.sms.CashOutSmsWatcher
) : ViewModel() {

    protected val _uiState = MutableStateFlow(UssdFlowUiState())
    val uiState: StateFlow<UssdFlowUiState> = _uiState.asStateFlow()

    private var idleResetJob: Job? = null

    init {
        refreshPermissions()
        viewModelScope.launch {
            checkUssdAccessUseCase.observe().collect { access ->
                _uiState.update { it.copy(subscriptionAccess = access) }
            }
        }
        viewModelScope.launch { refreshSubscriptionAccess() }
        viewModelScope.launch {
            accessibilityRepository.accessibilityStatus.collect { enabled ->
                _uiState.update { it.copy(accessibilityEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            automationEngine.status.collect { status ->
                _uiState.update {
                    it.copy(
                        automationStatus = status,
                        isLoading = status == AutomationStatus.STARTING ||
                            status == AutomationStatus.DIALING
                    )
                }
                when (status) {
                    AutomationStatus.WAITING_FOR_PIN ->
                        _uiState.update {
                            it.copy(infoMessage = "Enter your PIN on the USSD screen.")
                        }
                    AutomationStatus.SUCCESS ->
                        _uiState.update {
                            it.copy(infoMessage = "Transaction completed successfully.")
                        }
                    AutomationStatus.FAILED ->
                        _uiState.update {
                            it.copy(errorMessage = automationEngine.lastError.value ?: "Automation failed")
                        }
                    AutomationStatus.TIMEOUT ->
                        _uiState.update {
                            it.copy(errorMessage = "USSD automation timed out. Please try again.")
                        }
                    AutomationStatus.CANCELLED ->
                        _uiState.update {
                            it.copy(infoMessage = "Automation cancelled.")
                        }
                    AutomationStatus.SESSION_ENDED ->
                        _uiState.update {
                            it.copy(
                                infoMessage = "USSD session ended. You can start another transaction."
                            )
                        }
                    else -> Unit
                }
                if (status.isTerminal) {
                    scheduleIdleReset()
                }
            }
        }
    }

    private fun scheduleIdleReset() {
        idleResetJob?.cancel()
        idleResetJob = viewModelScope.launch {
            // Unlock UI immediately; briefly keep the status message then clear to Ready.
            _uiState.update { it.copy(isLoading = false) }
            delay(TERMINAL_RESET_DELAY_MS)
            automationEngine.acknowledgeIdle()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    infoMessage = null,
                    errorMessage = null
                )
            }
        }
    }

    fun refreshPermissions() {
        accessibilityRepository.refresh()
        _uiState.update {
            it.copy(callPermissionGranted = PermissionHelper.hasDialPermissions(context))
        }
        if (PermissionHelper.hasSmsPermission(context)) {
            cashOutSmsWatcher.onSmsPermissionGranted()
        }
        viewModelScope.launch { refreshSubscriptionAccess() }
    }

    protected suspend fun refreshSubscriptionAccess(): UssdAccessResult {
        val access = checkUssdAccessUseCase()
        _uiState.update { it.copy(subscriptionAccess = access) }
        return access
    }

    protected suspend fun blockedUssdAccess(): UssdAccessResult? {
        val access = refreshSubscriptionAccess()
        if (access == UssdAccessResult.ALLOWED) return null
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = access.userMessage,
                subscriptionAccess = access
            )
        }
        return access
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
        idleResetJob?.cancel()
        // Never block a new run behind a stuck previous session.
        if (automationEngine.status.value.isActive) {
            automationEngine.cancel()
        }
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
                    it.copy(errorMessage = "Grant phone and phone-state permissions to dial USSD on the selected SIM.")
                }
                return
            }
        }

        viewModelScope.launch {
            if (blockedUssdAccess() != null) return@launch
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
        if (!automationEngine.status.value.isActive) return
        idleResetJob?.cancel()
        automationEngine.cancel()
    }

    fun showSubscriptionBlocked() {
        viewModelScope.launch { blockedUssdAccess() }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    companion object {
        private const val TERMINAL_RESET_DELAY_MS = 800L
    }
}
