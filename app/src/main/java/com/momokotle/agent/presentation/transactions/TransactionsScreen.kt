package com.momokotle.agent.presentation.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onBack: (() -> Unit)? = null,
    isRootTab: Boolean = false,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isRootTab) "History" else "Transactions") },
                navigationIcon = {
                    if (!isRootTab && onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            FilterRow(
                selected = uiState.filter,
                onSelect = viewModel::setFilter
            )

            if (uiState.transactions.isEmpty()) {
                Text(
                    text = "No transactions yet",
                    modifier = Modifier.padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.transactions, key = { it.id }) { tx ->
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
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        FilterChip(
            selected = selected == null,
            onClick = { onSelect(null) },
            label = { Text("All") }
        )
        FilterChip(
            selected = selected == TransactionType.MTN_CASHIN,
            onClick = { onSelect(TransactionType.MTN_CASHIN) },
            label = { Text("Cash In") }
        )
        FilterChip(
            selected = selected == TransactionType.MTN_CASHOUT,
            onClick = { onSelect(TransactionType.MTN_CASHOUT) },
            label = { Text("Cash Out") }
        )
        FilterChip(
            selected = selected == TransactionType.AGENT_MERCHANT,
            onClick = { onSelect(TransactionType.AGENT_MERCHANT) },
            label = { Text("Merchant") }
        )
        FilterChip(
            selected = selected == TransactionType.BALANCE,
            onClick = { onSelect(TransactionType.BALANCE) },
            label = { Text("Balance") }
        )
        FilterChip(
            selected = selected == TransactionType.CUSTOM,
            onClick = { onSelect(TransactionType.CUSTOM) },
            label = { Text("Custom") }
        )
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = TransactionsViewModel.typeLabel(transaction.type),
                style = MaterialTheme.typography.titleMedium
            )
            transaction.flowName?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "GHS ${transaction.amount} · ${TransactionsViewModel.statusLabel(transaction.status)}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (transaction.profit > 0) {
                Text(
                    text = "Est. profit GHS ${transaction.profit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            transaction.phoneNumber?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = TransactionsViewModel.formatDate(transaction.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
