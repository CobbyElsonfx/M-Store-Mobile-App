package com.momokotle.agent.presentation.transactions

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSearchField
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppTransactionCard
import com.momokotle.agent.ui.components.statusToneForLabel
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun TransactionsScreen(
    onBack: (() -> Unit)? = null,
    isRootTab: Boolean = false,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(uiState.transactions, searchQuery) {
        if (searchQuery.isBlank()) uiState.transactions
        else {
            val q = searchQuery.trim().lowercase()
            uiState.transactions.filter { tx ->
                tx.phoneNumber?.lowercase()?.contains(q) == true ||
                    tx.amount.toString().contains(q) ||
                    TransactionsViewModel.typeLabel(tx.type).lowercase().contains(q) ||
                    tx.flowName?.lowercase()?.contains(q) == true
            }
        }
    }

    AppScaffold(
        title = if (isRootTab) "History" else "Transactions",
        onBack = if (!isRootTab) onBack else null
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.InputSpacing)
        ) {
            AppSearchField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search phone, amount, type…"
            )

            FilterRow(
                selected = uiState.filter,
                onSelect = viewModel::setFilter
            )

            if (filtered.isEmpty()) {
                AppEmptyState(
                    title = if (searchQuery.isNotBlank()) "No matching transactions" else "No transactions yet",
                    message = "Completed USSD actions appear here"
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimens.ListItemSpacing)) {
                    items(filtered, key = { it.id }) { tx ->
                        TransactionCard(transaction = tx)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selected: TransactionType?,
    onSelect: (TransactionType?) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        AppFilterChip(label = "All", selected = selected == null, onClick = { onSelect(null) })
        AppFilterChip(
            label = "Cash In",
            selected = selected == TransactionType.MTN_CASHIN,
            onClick = { onSelect(TransactionType.MTN_CASHIN) }
        )
        AppFilterChip(
            label = "Cash Out",
            selected = selected == TransactionType.MTN_CASHOUT,
            onClick = { onSelect(TransactionType.MTN_CASHOUT) }
        )
        AppFilterChip(
            label = "Merchant",
            selected = selected == TransactionType.AGENT_MERCHANT,
            onClick = { onSelect(TransactionType.AGENT_MERCHANT) }
        )
        AppFilterChip(
            label = "Balance",
            selected = selected == TransactionType.BALANCE,
            onClick = { onSelect(TransactionType.BALANCE) }
        )
        AppFilterChip(
            label = "Custom",
            selected = selected == TransactionType.CUSTOM,
            onClick = { onSelect(TransactionType.CUSTOM) }
        )
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val statusLabel = TransactionsViewModel.statusLabel(transaction.status)
    val profit = if (transaction.profit > 0) "Est. profit GHS ${transaction.profit}" else null
    AppTransactionCard(
        typeLabel = TransactionsViewModel.typeLabel(transaction.type),
        phoneNumber = transaction.phoneNumber,
        amount = "GHS ${transaction.amount}",
        profit = profit,
        statusLabel = statusLabel,
        statusTone = statusToneForLabel(statusLabel),
        dateTime = TransactionsViewModel.formatDate(transaction.timestamp),
        flowName = transaction.flowName
    )
}
