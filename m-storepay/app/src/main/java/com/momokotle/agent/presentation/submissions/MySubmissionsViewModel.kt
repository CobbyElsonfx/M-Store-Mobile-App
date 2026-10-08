package com.momokotle.agent.presentation.submissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.SubscriptionCheckout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MySubmissionsViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    val submissions: StateFlow<List<FormSubmission>> = authRepository.observeCachedProfile()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                formSubmissionRepository.observeMySubmissions(userId)
            }
        }
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

data class SubmissionDetailUiState(
    val submission: FormSubmission? = null,
    val checkout: SubscriptionCheckout? = null,
    val manualCheckout: ManualPaymentCheckout? = null,
    val accessCodeToLaunch: String? = null,
    val message: String? = null,
    val errorMessage: String? = null,
    val isPayingPaystack: Boolean = false,
    val isPayingManual: Boolean = false,
    val isCheckingStatus: Boolean = false,
    val copiedLabel: String? = null
) {
    val isBusy: Boolean get() = isPayingPaystack || isPayingManual || isCheckingStatus
}

@HiltViewModel
class SubmissionDetailViewModel @Inject constructor(
    private val formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmissionDetailUiState())
    val uiState: StateFlow<SubmissionDetailUiState> = _uiState.asStateFlow()

    fun loadSubmission(submissionId: String) {
        viewModelScope.launch {
            val local = formSubmissionRepository.getSubmission(submissionId)
            _uiState.update { it.copy(submission = local) }
            val refreshed = formSubmissionRepository.refreshSubmission(submissionId)
            if (refreshed != null) {
                _uiState.update { it.copy(submission = refreshed) }
            }
        }
    }

    fun startPaystackPayment() {
        val submission = _uiState.value.submission ?: return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPayingPaystack = true,
                    isPayingManual = false,
                    errorMessage = null,
                    message = null,
                    manualCheckout = null
                )
            }
            formSubmissionRepository.initializePayment(submission.submissionId)
                .onSuccess { checkout ->
                    _uiState.update {
                        it.copy(
                            isPayingPaystack = false,
                            checkout = checkout,
                            accessCodeToLaunch = checkout.accessCode.ifBlank { null },
                            message = if (checkout.accessCode.isBlank()) {
                                "Payment started. Reference ${checkout.reference}."
                            } else {
                                null
                            }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isPayingPaystack = false,
                            errorMessage = e.message ?: "Could not start Paystack payment"
                        )
                    }
                }
        }
    }

    fun startManualPayment() {
        val submission = _uiState.value.submission ?: return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPayingManual = true,
                    isPayingPaystack = false,
                    errorMessage = null,
                    message = null,
                    checkout = null,
                    accessCodeToLaunch = null
                )
            }
            formSubmissionRepository.initializeManualPayment(submission.submissionId)
                .onSuccess { checkout ->
                    _uiState.update {
                        it.copy(
                            isPayingManual = false,
                            manualCheckout = checkout,
                            message = "Pay with MoMo or bank using the details below, then wait for admin confirmation."
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isPayingManual = false,
                            errorMessage = e.message ?: "Could not start manual payment"
                        )
                    }
                }
        }
    }

    fun checkManualPaymentStatus() {
        val submissionId = _uiState.value.submission?.submissionId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingStatus = true, errorMessage = null) }
            val refreshed = formSubmissionRepository.refreshSubmission(submissionId)
            val paid = refreshed?.isPaid == true ||
                refreshed?.paymentStatus.equals("Confirmed", ignoreCase = true)
            _uiState.update {
                it.copy(
                    isCheckingStatus = false,
                    submission = refreshed ?: it.submission,
                    message = if (paid) {
                        "Payment confirmed. Your submission is being processed."
                    } else {
                        "Payment is still pending admin confirmation."
                    }
                )
            }
        }
    }

    fun copyToClipboard(context: android.content.Context, label: String, value: String) {
        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as android.content.ClipboardManager
        clipboard.setPrimaryClip(android.content.ClipData.newPlainText(label, value))
        _uiState.update { it.copy(copiedLabel = label) }
    }

    fun consumeCopied() {
        _uiState.update { it.copy(copiedLabel = null) }
    }

    fun consumeLaunch() {
        _uiState.update { it.copy(accessCodeToLaunch = null) }
    }

    fun onCheckoutCompleted() {
        viewModelScope.launch {
            val submissionId = _uiState.value.submission?.submissionId ?: return@launch
            val refreshed = formSubmissionRepository.refreshSubmission(submissionId)
            _uiState.update {
                it.copy(
                    submission = refreshed ?: it.submission,
                    message = "Payment submitted. Your submission will move forward once confirmed.",
                    errorMessage = null
                )
            }
        }
    }

    fun onCheckoutFailed(message: String?) {
        _uiState.update {
            it.copy(errorMessage = message ?: "Payment did not complete")
        }
    }

    fun onCheckoutCancelled() {
        _uiState.update {
            it.copy(message = "Payment cancelled. You can try again from this page.")
        }
    }
}
