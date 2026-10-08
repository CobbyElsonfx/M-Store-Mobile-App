package com.momokotle.agent.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.domain.usecase.RegisterUseCase
import com.momokotle.agent.domain.usecase.ResendRegistrationOtpUseCase
import com.momokotle.agent.domain.usecase.VerifyRegistrationOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RegisterStep {
    Form,
    Otp
}

data class RegisterUiState(
    val step: RegisterStep = RegisterStep.Form,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val referralCode: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val otpCode: String = "",
    val resendCooldownSeconds: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val registerSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyRegistrationOtpUseCase: VerifyRegistrationOtpUseCase,
    private val resendRegistrationOtpUseCase: ResendRegistrationOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private var resendJob: Job? = null

    fun onFirstNameChange(value: String) {
        _uiState.update { it.copy(firstName = value, errorMessage = null) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phoneNumber = value, errorMessage = null) }
    }

    fun onLocationChange(value: String) {
        _uiState.update { it.copy(location = value, errorMessage = null) }
    }

    fun onReferralCodeChange(value: String) {
        _uiState.update { it.copy(referralCode = value.uppercase(), errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }

    fun onOtpChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(6)
        _uiState.update { it.copy(otpCode = digits, errorMessage = null) }
    }

    fun backToForm() {
        resendJob?.cancel()
        _uiState.update {
            it.copy(
                step = RegisterStep.Form,
                otpCode = "",
                errorMessage = null,
                infoMessage = null,
                resendCooldownSeconds = 0
            )
        }
    }

    fun register() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, infoMessage = null, registerSuccess = false)
            }
            when (
                val result = registerUseCase(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    email = state.email,
                    phoneNumber = state.phoneNumber,
                    location = state.location,
                    password = state.password,
                    confirmPassword = state.confirmPassword,
                    referralCode = state.referralCode.takeIf { it.isNotBlank() }
                )
            ) {
                is AuthResult.PendingVerification -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            step = RegisterStep.Otp,
                            email = result.email,
                            infoMessage = result.message,
                            otpCode = ""
                        )
                    }
                    startResendCooldown()
                }
                is AuthResult.Success ->
                    _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
                is AuthResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun verifyOtp() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, infoMessage = null, registerSuccess = false)
            }
            when (val result = verifyRegistrationOtpUseCase(state.email, state.otpCode)) {
                is AuthResult.Success ->
                    _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
                is AuthResult.PendingVerification ->
                    _uiState.update {
                        it.copy(isLoading = false, infoMessage = result.message, errorMessage = null)
                    }
                is AuthResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun resendOtp() {
        val state = _uiState.value
        if (state.resendCooldownSeconds > 0 || state.email.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = resendRegistrationOtpUseCase(state.email)) {
                is AuthResult.PendingVerification -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            infoMessage = result.message,
                            otpCode = ""
                        )
                    }
                    startResendCooldown()
                }
                is AuthResult.Success ->
                    _uiState.update { it.copy(isLoading = false) }
                is AuthResult.Error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun consumeRegisterSuccess() {
        _uiState.update { it.copy(registerSuccess = false) }
    }

    private fun startResendCooldown(seconds: Int = 60) {
        resendJob?.cancel()
        resendJob = viewModelScope.launch {
            for (remaining in seconds downTo 0) {
                _uiState.update { it.copy(resendCooldownSeconds = remaining) }
                delay(1_000)
            }
        }
    }
}
