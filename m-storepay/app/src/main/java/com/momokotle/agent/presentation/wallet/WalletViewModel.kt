package com.momokotle.agent.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.usecase.ObserveWalletTransactionsUseCase
import com.momokotle.agent.domain.usecase.ObserveWalletUseCase
import com.momokotle.agent.domain.usecase.ObserveWithdrawalsUseCase
import com.momokotle.agent.domain.usecase.RequestWithdrawalUseCase
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.Wallet
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.models.WithdrawalRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val wallet: Wallet? = null,
    val transactions: List<WalletTransactionRecord> = emptyList(),
    val withdrawals: List<WithdrawalRequest> = emptyList(),
    val amount: String = "",
    val paymentNumber: String = "",
    val paymentProvider: PaymentProvider = PaymentProvider.MTN_MOMO,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeWalletUseCase: ObserveWalletUseCase,
    private val observeTransactionsUseCase: ObserveWalletTransactionsUseCase,
    private val observeWithdrawalsUseCase: ObserveWithdrawalsUseCase,
    private val requestWithdrawalUseCase: RequestWithdrawalUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(WalletUiState())

    val uiState: StateFlow<WalletUiState> = authRepository.observeAuthState()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) {
                flowOf(WalletUiState())
            } else {
                combine(
                    observeWalletUseCase(userId),
                    observeTransactionsUseCase(userId),
                    observeWithdrawalsUseCase(userId),
                    _formState
                ) { wallet, transactions, withdrawals, form ->
                    form.copy(wallet = wallet, transactions = transactions, withdrawals = withdrawals)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WalletUiState())

    fun onAmountChange(value: String) {
        _formState.update { it.copy(amount = value, errorMessage = null) }
    }

    fun onPaymentNumberChange(value: String) {
        _formState.update { it.copy(paymentNumber = value, errorMessage = null) }
    }

    fun onProviderChange(provider: PaymentProvider) {
        _formState.update { it.copy(paymentProvider = provider, errorMessage = null) }
    }

    fun requestWithdrawal(userId: String) {
        val amount = uiState.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _formState.update { it.copy(errorMessage = "Enter a valid amount") }
            return
        }
        val paymentNumber = uiState.value.paymentNumber.trim()
        if (paymentNumber.isBlank()) {
            _formState.update { it.copy(errorMessage = "Enter payment number") }
            return
        }
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            requestWithdrawalUseCase(
                userId = userId,
                amount = amount,
                paymentMethod = uiState.value.paymentProvider,
                paymentNumber = paymentNumber
            ).onSuccess {
                _formState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Withdrawal request submitted",
                        amount = "",
                        paymentNumber = ""
                    )
                }
            }.onFailure { error ->
                _formState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "Withdrawal failed")
                }
            }
        }
    }
}
