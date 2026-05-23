package com.momokotle.agent.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val filter: TransactionType? = null
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    transactionRepository: TransactionRepository
) : ViewModel() {

    private val filter = MutableStateFlow<TransactionType?>(null)

    val uiState: StateFlow<TransactionsUiState> = combine(
        transactionRepository.observeAll(),
        filter
    ) { transactions, selectedFilter ->
        TransactionsUiState(
            transactions = if (selectedFilter == null) {
                transactions
            } else {
                transactions.filter { it.type == selectedFilter }
            },
            filter = selectedFilter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TransactionsUiState())

    fun setFilter(type: TransactionType?) {
        filter.update { type }
    }

    companion object {
        fun formatDate(timestamp: Long): String {
            val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        fun typeLabel(type: TransactionType): String = when (type) {
            TransactionType.MTN_CASHIN -> "MTN Cashin"
            TransactionType.MTN_CASHOUT -> "MTN Cashout"
            TransactionType.AGENT_MERCHANT -> "Agent/Merchant"
            TransactionType.MTN_AIRTIME -> "MTN Airtime"
            TransactionType.BALANCE -> "Balance"
            TransactionType.COMMISSION -> "Commission"
            TransactionType.EVD_JUST4U -> "EVD/JUST4U"
            TransactionType.CUSTOM -> "Custom Menu"
        }

        fun statusLabel(status: TransactionStatus): String = when (status) {
            TransactionStatus.ATTEMPTED -> "Attempted"
            TransactionStatus.PENDING -> "Pending"
            TransactionStatus.SUCCESSFUL -> "Successful"
            TransactionStatus.FAILED -> "Failed"
        }
    }
}
