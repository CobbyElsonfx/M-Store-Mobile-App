package com.momokotle.agent.presentation.profit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitRule
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.utils.AmountFormatter
import com.momokotle.agent.utils.PercentFormatter
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.ErrorBanner
import com.momokotle.agent.ui.components.InfoBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfitCalculationScreen(
    onBack: () -> Unit,
    viewModel: ProfitCalculationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(title = "Profit Calculator", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            ProfitCalculatorSection(
                title = "Cash In Calculator",
                modeLabel = uiState.cashInMode.displayName,
                amount = uiState.cashInAmount,
                onAmountChange = viewModel::onCashInAmountChange,
                onCalculate = viewModel::calculateCashIn,
                result = uiState.cashInResult
            )
            ProfitCalculatorSection(
                title = "Cash Out Calculator",
                modeLabel = uiState.cashOutMode.displayName,
                amount = uiState.cashOutAmount,
                onAmountChange = viewModel::onCashOutAmountChange,
                onCalculate = viewModel::calculateCashOut,
                result = uiState.cashOutResult
            )
        }
    }
}

@Composable
private fun ProfitCalculatorSection(
    title: String,
    modeLabel: String,
    amount: String,
    onAmountChange: (String) -> Unit,
    onCalculate: () -> Unit,
    result: com.momokotle.agent.models.ProfitResult?
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Mode: $modeLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            AgentTextField(value = amount, onValueChange = onAmountChange, label = "Amount (GHS)")
            AgentActionButton(text = "Calculate", onClick = onCalculate)
            result?.let {
                val displayAmount = AmountFormatter.parse(amount)?.let { formatProfit(it) } ?: amount
                Text(
                    text = "Amount: GHS $displayAmount",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Profit: GHS ${formatProfit(it.profit)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                it.message?.let { msg -> ErrorBanner(message = msg) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashInSettingsScreen(
    onBack: () -> Unit,
    viewModel: CashInSettingsViewModel = hiltViewModel()
) {
    ProfitTypeSettingsScreen(
        title = "Cash In Settings",
        onBack = onBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashOutSettingsScreen(
    onBack: () -> Unit,
    viewModel: CashOutSettingsViewModel = hiltViewModel()
) {
    ProfitTypeSettingsScreen(
        title = "Cash Out Settings",
        onBack = onBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfitTypeSettingsScreen(
    title: String,
    onBack: () -> Unit,
    viewModel: ProfitTypeSettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.message, uiState.errorMessage) {
        if (uiState.message != null || uiState.errorMessage != null) {
            kotlinx.coroutines.delay(2500)
            viewModel.clearMessage()
        }
    }

    AppScaffold(title = title, onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            uiState.message?.let { AppInfoBanner(it) }
            uiState.errorMessage?.let { AppErrorBanner(it) }

            CalculationModeSection(
                selectedMode = uiState.calculationMode,
                onModeSelected = viewModel::setCalculationMode
            )

            DefaultSettingsSection(
                percentInput = uiState.defaultPercentInput,
                onPercentChange = viewModel::onDefaultPercentChange,
                onSaveDefaultPercent = viewModel::saveDefaultPercent
            )

            CustomizeSettingsSection(
                enabled = uiState.calculationMode == ProfitCalculationMode.CUSTOMIZE,
                draftRule = uiState.draftRule,
                pendingRules = uiState.pendingRules,
                onDraftAmountChange = viewModel::onDraftAmountChange,
                onDraftProfitPercentChange = viewModel::onDraftProfitPercentChange,
                onAddRule = viewModel::addRule,
                onRemoveRule = viewModel::removePendingRule,
                onSaveSettings = viewModel::saveSettings
            )
        }
    }
}

@Composable
private fun CalculationModeSection(
    selectedMode: ProfitCalculationMode,
    onModeSelected: (ProfitCalculationMode) -> Unit
) {
    SettingsBlock(title = "Calculation Mode") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ProfitCalculationMode.entries.forEach { mode ->
                AppFilterChip(
                    label = mode.displayName,
                    selected = selectedMode == mode,
                    onClick = { onModeSelected(mode) }
                )
            }
        }
    }
}

@Composable
private fun DefaultSettingsSection(
    percentInput: String,
    onPercentChange: (String) -> Unit,
    onSaveDefaultPercent: () -> Unit
) {
    SettingsBlock(title = "Default Settings") {
        Text(
            text = "Profit = Amount × (Percentage ÷ 100)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        AgentTextField(
            value = percentInput,
            onValueChange = onPercentChange,
            label = "Default percentage (%)"
        )
        AgentActionButton(text = "Save Default Percentage", onClick = onSaveDefaultPercent)
        val rate = PercentFormatter.parsePercentInput(percentInput)
        if (rate != null) {
            Text(
                text = "Example: 300 GHS at ${PercentFormatter.toLabel(rate)} → " +
                    "${formatProfit(PercentFormatter.calculateProfit(300.0, rate))} GHS profit",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun CustomizeSettingsSection(
    enabled: Boolean,
    draftRule: DraftProfitRule,
    pendingRules: List<ProfitRule>,
    onDraftAmountChange: (String) -> Unit,
    onDraftProfitPercentChange: (String) -> Unit,
    onAddRule: () -> Unit,
    onRemoveRule: (Double) -> Unit,
    onSaveSettings: () -> Unit
) {
    SettingsBlock(title = "Customize Settings") {
        if (!enabled) {
            Text(
                text = "Switch to Customize mode to add custom rules.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            return@SettingsBlock
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AgentTextField(
                value = draftRule.amount,
                onValueChange = onDraftAmountChange,
                label = "Amount (GHS)",
                modifier = Modifier.weight(1f)
            )
            AgentTextField(
                value = draftRule.profitPercent,
                onValueChange = onDraftProfitPercentChange,
                label = "Profit (%)",
                modifier = Modifier.weight(1f)
            )
        }
        AgentActionButton(text = "Add Rule", onClick = onAddRule)
        if (pendingRules.isNotEmpty()) {
            Text("Custom rules", style = MaterialTheme.typography.labelLarge)
            pendingRules.forEach { rule ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatProfit(rule.amount)} GHS → ${PercentFormatter.toLabel(rule.profitPercentage)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(onClick = { onRemoveRule(rule.amount) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
        AgentActionButton(text = "Save Settings", onClick = onSaveSettings)
    }
}

@Composable
private fun SettingsBlock(
    title: String,
    content: @Composable () -> Unit
) {
    AppCard {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        content()
    }
}

private fun formatProfit(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
