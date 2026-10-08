package com.momokotle.agent.presentation.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.usecase.CheckSubscriptionPaymentStatusUseCase
import com.momokotle.agent.domain.usecase.InitializeSubscriptionPaymentUseCase
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase
import com.momokotle.agent.domain.usecase.ObserveSubscriptionPlansUseCase
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase
import com.momokotle.agent.models.BusinessSettings
import com.momokotle.agent.models.SubscriptionCheckout
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.UserProfile
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubscriptionUiState(
    val plans: List<SubscriptionPlan> = emptyList(),
    val settings: BusinessSettings = BusinessSettings(),
    val profile: UserProfile? = null,
    val selectedPlanId: String? = null,
    val checkout: SubscriptionCheckout? = null,
    val paymentStatus: SubscriptionPaymentStatus? = null,
    val accessCodeToLaunch: String? = null,
    val isLoading: Boolean = false,
    val isCheckingStatus: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val purchaseSuccess: Boolean = false
)

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observePlansUseCase: ObserveSubscriptionPlansUseCase,
    private val observeSettingsUseCase: ObserveBusinessSettingsUseCase,
    private val initializePaymentUseCase: InitializeSubscriptionPaymentUseCase,
    private val checkPaymentStatusUseCase: CheckSubscriptionPaymentStatusUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _actionState = MutableStateFlow(SubscriptionUiState())
    val actionState: StateFlow<SubscriptionUiState> = _actionState.asStateFlow()

    val uiState: StateFlow<SubscriptionUiState> = combine(
        observePlansUseCase(),
        observeSettingsUseCase(),
        authRepository.observeAuthState(),
        _actionState
    ) { plans, settings, profile, action ->
        action.copy(plans = plans, settings = settings, profile = profile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SubscriptionUiState())

    fun selectPlan(planId: String) {
        _actionState.update {
            it.copy(selectedPlanId = planId, errorMessage = null, infoMessage = null)
        }
    }

    fun onScreenVisible() {
        if (_actionState.value.isLoading ||
            _actionState.value.isCheckingStatus ||
            _actionState.value.accessCodeToLaunch != null
        ) {
            return
        }
        viewModelScope.launch {
            refreshUserProfileUseCase()
            val reference = _actionState.value.checkout?.reference
                ?: _actionState.value.paymentStatus?.reference
            if (!reference.isNullOrBlank() && !_actionState.value.purchaseSuccess) {
                refreshPaymentStatus(reference, poll = false)
            }
        }
    }

    fun payNow() {
        val planId = uiState.value.selectedPlanId ?: run {
            _actionState.update { it.copy(errorMessage = "Select a subscription plan") }
            return
        }
        if (!networkMonitor.isOnline()) {
            _actionState.update { it.copy(errorMessage = InternetMessages.SUBSCRIPTION_REQUIRES_INTERNET) }
            return
        }
        viewModelScope.launch {
            _actionState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    infoMessage = null,
                    purchaseSuccess = false,
                    accessCodeToLaunch = null
                )
            }
            initializePaymentUseCase(planId)
                .onSuccess { checkout ->
                    if (checkout.accessCode.isBlank() || checkout.publicKey.isBlank()) {
                        _actionState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Paystack checkout was not initialized by the server."
                            )
                        }
                        return@onSuccess
                    }
                    _actionState.update {
                        it.copy(
                            isLoading = false,
                            checkout = checkout,
                            paymentStatus = SubscriptionPaymentStatus(
                                reference = checkout.reference,
                                status = checkout.status,
                                amount = checkout.amount,
                                currency = checkout.currency,
                                planName = checkout.planName
                            ),
                            accessCodeToLaunch = checkout.accessCode,
                            infoMessage = "Opening Paystack checkout…"
                        )
                    }
                }
                .onFailure { error ->
                    _actionState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Could not start payment")
                    }
                }
        }
    }

    fun consumeLaunch() {
        _actionState.update { it.copy(accessCodeToLaunch = null) }
    }

    fun onCheckoutCompleted(paystackReference: String? = null) {
        val reference = paystackReference?.takeIf { it.isNotBlank() }
            ?: _actionState.value.checkout?.reference
            ?: return
        _actionState.update {
            it.copy(infoMessage = "Verifying payment…", errorMessage = null)
        }
        refreshPaymentStatus(reference, poll = true)
    }

    fun onCheckoutCancelled() {
        _actionState.update {
            it.copy(
                errorMessage = null,
                infoMessage = "Payment was cancelled. Your subscription was not activated."
            )
        }
    }

    fun onCheckoutFailed(message: String?) {
        _actionState.update {
            it.copy(
                errorMessage = message ?: "Payment failed",
                infoMessage = null
            )
        }
    }

    fun checkStatus() {
        val reference = _actionState.value.checkout?.reference ?: _actionState.value.paymentStatus?.reference
        if (reference.isNullOrBlank()) {
            _actionState.update { it.copy(errorMessage = "No payment to check yet") }
            return
        }
        refreshPaymentStatus(reference, poll = false)
    }

    fun consumePurchaseSuccess() {
        _actionState.update { it.copy(purchaseSuccess = false) }
    }

    private fun refreshPaymentStatus(reference: String, poll: Boolean) {
        viewModelScope.launch {
            _actionState.update { it.copy(isCheckingStatus = true, errorMessage = null) }
            var last: SubscriptionPaymentStatus? = null
            val attempts = if (poll) 8 else 1
            repeat(attempts) { index ->
                val result = checkPaymentStatusUseCase(reference)
                last = result.getOrNull()
                if (result.isFailure) {
                    _actionState.update {
                        it.copy(
                            isCheckingStatus = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Could not check payment"
                        )
                    }
                    return@launch
                }
                val status = last ?: return@repeat
                applyServerStatus(status)
                if (!status.isPending) {
                    return@launch
                }
                if (poll && index < attempts - 1) {
                    delay(1_500)
                }
            }
            _actionState.update {
                it.copy(
                    isCheckingStatus = false,
                    infoMessage = last?.let {
                        "Payment is being processed. We’ll confirm your subscription shortly."
                    }
                )
            }
        }
    }

    private suspend fun refreshProfileUntilFullAccess() {
        repeat(6) { index ->
            refreshUserProfileUseCase()
            if (authRepository.getCurrentUser()?.hasFullAccess == true) return
            if (index < 5) delay(750)
        }
    }

    private suspend fun applyServerStatus(status: SubscriptionPaymentStatus) {
        when {
            status.isConfirmed && status.subscriptionActive -> {
                refreshProfileUntilFullAccess()
                _actionState.update {
                    it.copy(
                        isCheckingStatus = false,
                        paymentStatus = status,
                        purchaseSuccess = true,
                        infoMessage = "Subscription activated.",
                        errorMessage = null
                    )
                }
            }
            status.isConfirmed && !status.subscriptionActive -> {
                refreshProfileUntilFullAccess()
                val active = authRepository.getCurrentUser()?.hasFullAccess == true
                _actionState.update {
                    it.copy(
                        isCheckingStatus = false,
                        paymentStatus = status,
                        purchaseSuccess = active,
                        infoMessage = if (active) {
                            "Subscription activated."
                        } else {
                            "Payment received. Confirming your subscription…"
                        },
                        errorMessage = null
                    )
                }
            }
            status.isUnsuccessful -> {
                _actionState.update {
                    it.copy(
                        isCheckingStatus = false,
                        paymentStatus = status,
                        purchaseSuccess = false,
                        errorMessage = unsuccessfulMessage(status.status),
                        infoMessage = null
                    )
                }
            }
            else -> {
                _actionState.update {
                    it.copy(paymentStatus = status, isCheckingStatus = false)
                }
            }
        }
    }

    private fun unsuccessfulMessage(status: String): String = when (status.lowercase()) {
        "abandoned" -> "Payment was abandoned. Your subscription was not activated."
        "reversed" -> "Payment was reversed. Your subscription was not activated."
        else -> "Payment failed. Your subscription was not activated."
    }
}
