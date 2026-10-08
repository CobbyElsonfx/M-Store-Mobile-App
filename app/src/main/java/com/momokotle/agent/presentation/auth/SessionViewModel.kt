package com.momokotle.agent.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.domain.usecase.ObserveAuthStateUseCase
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase
import com.momokotle.agent.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val authError: String? = null
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    private fun startDataSync(userId: String) {
        productRepository.startSync()
        orderRepository.startSync(userId)
        formSubmissionRepository.startSync(userId)
    }

    private fun stopDataSync() {
        productRepository.stopSync()
        orderRepository.stopSync()
        formSubmissionRepository.stopSync()
    }

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { profile ->
                when {
                    profile == null -> {
                        stopDataSync()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isAuthenticated = false,
                                authError = null
                            )
                        }
                    }
                    profile.isSuspended -> {
                        stopDataSync()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isAuthenticated = false,
                                authError = AuthRepositoryImpl.SUSPENDED_MESSAGE
                            )
                        }
                    }
                    else -> {
                        startDataSync(profile.userId)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isAuthenticated = true,
                                authError = null
                            )
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
            if (authRepository.getCurrentUserId() == null) {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = false) }
                return@launch
            }
            when (val result = refreshUserProfileUseCase()) {
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = result.message != AuthRepositoryImpl.SUSPENDED_MESSAGE,
                            authError = if (result.message == AuthRepositoryImpl.SUSPENDED_MESSAGE) {
                                result.message
                            } else {
                                null
                            }
                        )
                    }
                }
                is AuthResult.Success -> {
                    startDataSync(result.profile.userId)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            authError = null
                        )
                    }
                }
            }
        }
    }

    fun clearAuthError() {
        _uiState.update { it.copy(authError = null) }
    }
}
