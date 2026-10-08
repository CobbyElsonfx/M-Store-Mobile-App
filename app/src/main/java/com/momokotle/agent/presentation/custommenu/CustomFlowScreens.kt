package com.momokotle.agent.presentation.custommenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun CustomFlowListScreen(
    onBack: () -> Unit,
    onRunFlow: (Long) -> Unit,
    viewModel: CustomFlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()
    val flows by viewModel.customFlows.collectAsStateWithLifecycle()

    AppScaffold(title = "Custom Menu", onBack = onBack) { padding ->
        if (isEditing) {
            CustomFlowEditForm(
                formState = formState,
                onNameChange = viewModel::onNameChange,
                onUssdCodeChange = viewModel::onUssdCodeChange,
                onStep1Change = viewModel::onStep1Change,
                onStep2Change = viewModel::onStep2Change,
                onStep3Change = viewModel::onStep3Change,
                onDynamicPhoneChange = viewModel::onDynamicPhoneChange,
                onDynamicAmountChange = viewModel::onDynamicAmountChange,
                onSave = viewModel::saveFlow,
                onCancel = viewModel::cancelEdit,
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(AppDimens.ListItemSpacing)
            ) {
                Text(
                    text = "Add, edit, or run your custom USSD menus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                uiState.infoMessage?.let { AppInfoBanner(it) }

                AgentActionButton(text = "Add Custom Flow", onClick = viewModel::startNewFlow)

                if (flows.isEmpty()) {
                    Text(
                        text = "No custom flows yet",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(flows, key = { it.id }) { flow ->
                            CustomFlowCard(
                                flow = flow,
                                onRun = { onRunFlow(flow.id) },
                                onEdit = { viewModel.editFlow(flow) },
                                onDelete = { viewModel.deleteFlow(flow.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomFlowEditForm(
    formState: CustomFlowFormState,
    onNameChange: (String) -> Unit,
    onUssdCodeChange: (String) -> Unit,
    onStep1Change: (String) -> Unit,
    onStep2Change: (String) -> Unit,
    onStep3Change: (String) -> Unit,
    onDynamicPhoneChange: (Boolean) -> Unit,
    onDynamicAmountChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AgentTextField(value = formState.name, onValueChange = onNameChange, label = "Menu Name")
        AgentTextField(value = formState.ussdCode, onValueChange = onUssdCodeChange, label = "USSD Code")
        AgentTextField(value = formState.step1, onValueChange = onStep1Change, label = "Step 1")
        AgentTextField(value = formState.step2, onValueChange = onStep2Change, label = "Step 2")
        AgentTextField(value = formState.step3, onValueChange = onStep3Change, label = "Step 3")
        Text(
            text = "Use phone or amount as step values. Example: 3, 1, phone, phone, amount",
            style = MaterialTheme.typography.bodySmall
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = formState.useDynamicPhone, onCheckedChange = onDynamicPhoneChange)
            Text("Dynamic Phone Number")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = formState.useDynamicAmount, onCheckedChange = onDynamicAmountChange)
            Text("Dynamic Amount")
        }
        AgentActionButton(text = "Save Custom Flow", onClick = onSave)
        AgentActionButton(text = "Cancel", onClick = onCancel)
    }
}

@Composable
private fun CustomFlowCard(
    flow: CustomFlow,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AppCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(flow.name, style = MaterialTheme.typography.titleMedium)
            Text(
                "${flow.ussdCode} → ${flow.step1}, ${flow.step2}, ${flow.step3}".trimEnd(',', ' '),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onRun) { Text("Run") }
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}
