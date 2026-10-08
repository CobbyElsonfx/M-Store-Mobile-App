package com.momokotle.agent.presentation.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.PaymentProvider
import com.momokotle.agent.models.WalletTransactionRecord
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    onBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userId = uiState.wallet?.userId

    AppScaffold(title = "Wallet", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppSectionHeader(title = "Balances")
                    BalanceRow("Available", uiState.wallet?.availableBalance, AppColors.Success)
                    BalanceRow("Pending", uiState.wallet?.pendingBalance, AppColors.Warning)
                    BalanceRow("Withdrawn", uiState.wallet?.withdrawnBalance, AppFormStyles.label)
                    BalanceRow("Lifetime earnings", uiState.wallet?.lifetimeEarnings, AppFormStyles.onSurface)
                }
            }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSectionHeader(
                        title = "Request withdrawal",
                        subtitle = "Enter amount and destination, then submit."
                    )
                    AppTextField(
                        value = uiState.amount,
                        onValueChange = viewModel::onAmountChange,
                        label = "Amount (GHS)"
                    )
                    AppTextField(
                        value = uiState.paymentNumber,
                        onValueChange = viewModel::onPaymentNumberChange,
                        label = "Payment number"
                    )
                    Text(
                        text = "Payout method",
                        style = MaterialTheme.typography.labelLarge,
                        color = AppFormStyles.onSurface
                    )
                    PaymentProvider.walletProviders.forEach { provider ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(provider.displayName, color = AppFormStyles.onSurface)
                            RadioButton(
                                selected = uiState.paymentProvider == provider,
                                onClick = { viewModel.onProviderChange(provider) }
                            )
                        }
                    }
                    uiState.errorMessage?.let { AppErrorBanner(message = it) }
                    uiState.successMessage?.let {
                        Text(it, color = AppColors.Success, fontWeight = FontWeight.SemiBold)
                    }
                    if (uiState.isLoading) {
                        AppLoadingState()
                    } else {
                        AppButton(
                            text = "Submit withdrawal",
                            onClick = { userId?.let(viewModel::requestWithdrawal) },
                            enabled = userId != null,
                            style = AppButtonStyle.Accent
                        )
                    }
                }
            }

            AppSectionHeader(title = "Transaction history")
            if (uiState.transactions.isEmpty()) {
                AppCard {
                    Text("No transactions yet", color = AppFormStyles.label)
                }
            } else {
                uiState.transactions.forEach { tx ->
                    TransactionRow(tx)
                }
            }
        }
    }
}

@Composable
private fun BalanceRow(
    label: String,
    amount: Double?,
    amountColor: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = AppFormStyles.label)
        Text(
            "GH₵ %.2f".format(amount ?: 0.0),
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}

@Composable
private fun TransactionRow(tx: WalletTransactionRecord) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(tx.description, fontWeight = FontWeight.SemiBold, color = AppFormStyles.onSurface)
            Text(
                "${tx.source.firestoreValue} · GH₵ %.2f".format(tx.amount),
                color = AppFormStyles.onSurface
            )
            Text(
                SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(tx.createdAt)),
                style = MaterialTheme.typography.bodySmall,
                color = AppFormStyles.label
            )
        }
    }
}
