package com.momokotle.agent.presentation.transactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.CommissionSummary
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSearchField
import com.momokotle.agent.ui.components.AppTransactionCard
import com.momokotle.agent.ui.components.statusToneForLabel
import com.momokotle.agent.ui.theme.AppColors
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

            PeriodTotalsRow(
                today = uiState.todaySummary,
                week = uiState.weekSummary
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
private fun PeriodTotalsRow(
    today: CommissionSummary,
    week: CommissionSummary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodTotalsCard(
            title = "Today",
            summary = today,
            modifier = Modifier.weight(1f)
        )
        PeriodTotalsCard(
            title = "This week",
            summary = week,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PeriodTotalsCard(
    title: String,
    summary: CommissionSummary,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TotalsMetricChip(
                label = "Cash In",
                amount = summary.cashInTotal,
                container = AppColors.SuccessContainer,
                accent = AppColors.Success
            )
            TotalsMetricChip(
                label = "Cash Out",
                amount = summary.cashOutTotal,
                container = AppColors.InfoContainer,
                accent = AppColors.Info
            )
            TotalsMetricChip(
                label = "Commission",
                amount = summary.totalCommission,
                container = AppColors.WarningContainer,
                accent = AppColors.Warning
            )
        }
    }
}

@Composable
private fun TotalsMetricChip(
    label: String,
    amount: Double,
    container: Color,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(container, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = accent,
            maxLines = 1
        )
        Text(
            text = "GHS ${"%.2f".format(amount)}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.FormInputText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val statusLabel = TransactionsViewModel.statusLabel(transaction.status)
    val profit = when {
        transaction.status == TransactionStatus.SUCCESSFUL && transaction.commission > 0 ->
            "Commission GHS ${"%.2f".format(transaction.commission)}"
        transaction.status == TransactionStatus.SUCCESSFUL ->
            "No commission"
        else -> null
    }
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
