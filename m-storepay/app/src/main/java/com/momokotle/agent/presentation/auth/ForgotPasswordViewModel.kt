package com.momokotle.agent.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val phone: String = "",
    val otpCode: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val codeSent: Boolean = false,
    val isLoading: Boolean = false,
    val infoMessage: String? = null,
    val errorMessage: String? = null,
    val resetComplete: Boolean = false
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun onOtpChange(value: String) {
        _uiState.update { it.copy(otpCode = value.filter { char -> char.isDigit() }.take(6), errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }

    fun requestCode() {
        val phone = _uiState.value.phone.trim()
        if (!PhoneNumberFormatter.isValidGhanaNumber(phone)) {
            _uiState.update { it.copy(errorMessage = "Enter a valid Ghana phone number") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            when (val result = authRepository.requestPasswordReset(phone)) {
                is AuthResult.Message -> _uiState.update {
                    it.copy(isLoading = false, codeSent = true, infoMessage = result.message)
                }
                is AuthResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetPassword() {
        val state = _uiState.value
        val error = when {
            state.otpCode.length != 6 -> "Enter the 6-digit code from the SMS"
            state.password.length < 6 -> "Password must be at least 6 characters"
            state.password != state.confirmPassword -> "Passwords do not match"
            else -> null
        }
        if (error != null) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authRepository.resetPassword(state.phone.trim(), state.otpCode, state.password)) {
                is AuthResult.Message -> _uiState.update {
                    it.copy(isLoading = false, resetComplete = true, infoMessage = result.message)
                }
                is AuthResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun consumeResetComplete() {
        _uiState.update { it.copy(resetComplete = false) }
    }
}
