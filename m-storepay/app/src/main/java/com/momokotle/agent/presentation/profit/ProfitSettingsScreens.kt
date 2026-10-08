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

    AppScaffold(title = "Commission Preview", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            ProfitCalculatorSection(
                title = "Cash In Preview",
                modeLabel = uiState.cashInMode.displayName,
                amount = uiState.cashInAmount,
                onAmountChange = viewModel::onCashInAmountChange,
                onCalculate = viewModel::previewCashIn,
                result = uiState.cashInResult
            )
            ProfitCalculatorSection(
                title = "Cash Out Preview",
                modeLabel = uiState.cashOutMode.displayName,
                amount = uiState.cashOutAmount,
                onAmountChange = viewModel::onCashOutAmountChange,
                onCalculate = viewModel::previewCashOut,
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
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Mode: $modeLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AgentTextField(value = amount, onValueChange = onAmountChange, label = "Amount (GHS)")
            AgentActionButton(text = "Preview", onClick = onCalculate)
            result?.let {
                val displayAmount = AmountFormatter.parse(amount)?.let { formatProfit(it) } ?: amount
                Text(
                    text = "Amount: GHS $displayAmount",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Commission: GHS ${formatProfit(it.profit)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = com.momokotle.agent.ui.theme.AppColors.Success
                )
                it.message?.let { msg -> InfoBanner(message = msg) }
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
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            uiState.message?.let { AppInfoBanner(it) }
            uiState.errorMessage?.let { AppErrorBanner(it) }

            CalculationModeSection(
                selectedMode = uiState.calculationMode,
                onModeSelected = viewModel::setCalculationMode
            )

            when (uiState.calculationMode) {
                ProfitCalculationMode.PERCENTAGE -> DefaultPercentSection(
                    percentInput = uiState.defaultPercentInput,
                    onPercentChange = viewModel::onDefaultPercentChange,
                    onSave = viewModel::saveDefaultPercent
                )
                ProfitCalculationMode.FIXED -> DefaultFixedSection(
                    fixedInput = uiState.defaultFixedInput,
                    onFixedChange = viewModel::onDefaultFixedChange,
                    onSave = viewModel::saveDefaultFixed
                )
                ProfitCalculationMode.TIERED -> TieredSettingsSection(
                    draftRule = uiState.draftRule,
                    pendingRules = uiState.pendingRules,
                    onDraftMinChange = viewModel::onDraftMinChange,
                    onDraftMaxChange = viewModel::onDraftMaxChange,
                    onDraftProfitPercentChange = viewModel::onDraftProfitPercentChange,
                    onDraftFixedCommissionChange = viewModel::onDraftFixedCommissionChange,
                    onUseFixedChange = viewModel::setDraftUseFixedCommission,
                    onAddRule = viewModel::addRule,
                    onRemoveRule = viewModel::removePendingRule,
                    onSaveSettings = viewModel::saveSettings
                )
            }
        }
    }
}

@Composable
private fun CalculationModeSection(
    selectedMode: ProfitCalculationMode,
    onModeSelected: (ProfitCalculationMode) -> Unit
) {
    SettingsBlock(
        title = "Calculation Mode",
        subtitle = "Choose how commission is calculated for this service."
    ) {
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
private fun DefaultPercentSection(
    percentInput: String,
    onPercentChange: (String) -> Unit,
    onSave: () -> Unit
) {
    SettingsBlock(title = "Percentage Settings") {
        Text(
            text = "Commission = Amount × (Percentage ÷ 100)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        AgentTextField(
            value = percentInput,
            onValueChange = onPercentChange,
            label = "Commission percentage (%)"
        )
        AgentActionButton(text = "Save Percentage", onClick = onSave)
    }
}

@Composable
private fun DefaultFixedSection(
    fixedInput: String,
    onFixedChange: (String) -> Unit,
    onSave: () -> Unit
) {
    SettingsBlock(title = "Fixed Commission") {
        Text(
            text = "Every successful transaction earns this fixed commission.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        AgentTextField(
            value = fixedInput,
            onValueChange = onFixedChange,
            label = "Fixed commission (GHS)"
        )
        AgentActionButton(text = "Save Fixed Amount", onClick = onSave)
    }
}

@Composable
private fun TieredSettingsSection(
    draftRule: DraftProfitRule,
    pendingRules: List<ProfitRule>,
    onDraftMinChange: (String) -> Unit,
    onDraftMaxChange: (String) -> Unit,
    onDraftProfitPercentChange: (String) -> Unit,
    onDraftFixedCommissionChange: (String) -> Unit,
    onUseFixedChange: (Boolean) -> Unit,
    onAddRule: () -> Unit,
    onRemoveRule: (ProfitRule) -> Unit,
    onSaveSettings: () -> Unit
) {
    SettingsBlock(title = "Tiered Rules") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppFilterChip(
                label = "Percentage tier",
                selected = !draftRule.useFixedCommission,
                onClick = { onUseFixedChange(false) }
            )
            AppFilterChip(
                label = "Fixed tier",
                selected = draftRule.useFixedCommission,
                onClick = { onUseFixedChange(true) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AgentTextField(
                value = draftRule.minAmount,
                onValueChange = onDraftMinChange,
                label = "Min (GHS)",
                modifier = Modifier.weight(1f)
            )
            AgentTextField(
                value = draftRule.maxAmount,
                onValueChange = onDraftMaxChange,
                label = "Max (GHS)",
                modifier = Modifier.weight(1f)
            )
        }
        if (draftRule.useFixedCommission) {
            AgentTextField(
                value = draftRule.fixedCommission,
                onValueChange = onDraftFixedCommissionChange,
                label = "Fixed commission (GHS)"
            )
        } else {
            AgentTextField(
                value = draftRule.profitPercent,
                onValueChange = onDraftProfitPercentChange,
                label = "Commission (%)"
            )
        }
        AgentActionButton(text = "Add Tier", onClick = onAddRule)
        if (pendingRules.isNotEmpty()) {
            Text("Saved tiers", style = MaterialTheme.typography.labelLarge)
            pendingRules.forEach { rule ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(rule.ruleLabel(), style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = { onRemoveRule(rule) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
        AgentActionButton(text = "Save Tiers", onClick = onSaveSettings)
    }
}

@Composable
private fun SettingsBlock(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    AppCard {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = com.momokotle.agent.ui.theme.AppFormStyles.onSurface
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.momokotle.agent.ui.theme.AppFormStyles.label
                    )
                }
            }
            content()
        }
    }
}

private fun formatProfit(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
