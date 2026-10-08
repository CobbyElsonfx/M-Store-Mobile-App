package com.momokotle.agent.presentation.referral

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun ReferralScreen(
    onBack: () -> Unit,
    onNavigateToWallet: () -> Unit = {},
    viewModel: ReferralViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    AppScaffold(title = "Referral Center", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            AppSectionHeader(
                title = "Share & earn",
                subtitle = "Invite agents with your permanent referral code"
            )

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Your Referral Code", style = MaterialTheme.typography.labelMedium)
                    CopyRow(
                        value = uiState.referralCode.ifBlank { "—" },
                        onCopy = {
                            viewModel.copyToClipboard(context, "Referral Code", uiState.referralCode)
                        }
                    )
                    Text("Referral Link", style = MaterialTheme.typography.labelMedium)
                    CopyRow(
                        value = uiState.referralLink.ifBlank { "—" },
                        onCopy = {
                            viewModel.copyToClipboard(context, "Referral Link", uiState.referralLink)
                        }
                    )
                }
            }

            AppButton(
                text = "Share Link",
                onClick = { viewModel.shareReferralLink(context, uiState.referralLink) },
                enabled = uiState.referralLink.isNotBlank(),
                style = AppButtonStyle.Accent
            )

            AppSectionHeader(title = "Statistics")
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatRow("Total referrals", uiState.referralInfo?.stats?.totalReferrals?.toString() ?: "0")
                    StatRow("Active referrals", uiState.referralInfo?.stats?.activeReferrals?.toString() ?: "0")
                    StatRow("Expired referrals", uiState.referralInfo?.stats?.expiredReferrals?.toString() ?: "0")
                    StatRow("Today's earnings", formatMoney(uiState.referralInfo?.stats?.todayEarnings))
                    StatRow("Monthly earnings", formatMoney(uiState.referralInfo?.stats?.monthlyEarnings))
                    StatRow("Lifetime earnings", formatMoney(uiState.referralInfo?.stats?.lifetimeEarnings))
                }
            }

            AppSectionHeader(title = "Wallet")
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatRow("Available", formatMoney(uiState.referralInfo?.wallet?.availableBalance))
                    StatRow("Pending", formatMoney(uiState.referralInfo?.wallet?.pendingBalance))
                    StatRow("Withdrawn", formatMoney(uiState.referralInfo?.wallet?.withdrawnBalance))
                }
            }

            AppButton(
                text = "Open Wallet",
                onClick = onNavigateToWallet,
                style = AppButtonStyle.Outlined
            )
        }
    }
}

@Composable
private fun CopyRow(value: String, onCopy: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onCopy) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

private fun formatMoney(amount: Double?): String = "GH₵ %.2f".format(amount ?: 0.0)
