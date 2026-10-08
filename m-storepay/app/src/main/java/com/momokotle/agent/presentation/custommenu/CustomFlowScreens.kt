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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.appFormCheckboxColors

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
                errorMessage = uiState.errorMessage,
                onNameChange = viewModel::onNameChange,
                onUssdCodeChange = viewModel::onUssdCodeChange,
                onStepChange = viewModel::onStepChange,
                onAddStep = viewModel::addStep,
                onRemoveStep = viewModel::removeStep,
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
                uiState.errorMessage?.let { AppErrorBanner(it) }

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
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onUssdCodeChange: (String) -> Unit,
    onStepChange: (Int, String) -> Unit,
    onAddStep: () -> Unit,
    onRemoveStep: (Int) -> Unit,
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
        errorMessage?.let { AppErrorBanner(it) }
        AgentTextField(value = formState.name, onValueChange = onNameChange, label = "Menu Name")
        AgentTextField(value = formState.ussdCode, onValueChange = onUssdCodeChange, label = "USSD Code")

        Text(
            text = "Steps (${formState.steps.size}/${CustomFlow.MAX_STEPS})",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        formState.steps.forEachIndexed { index, step ->
            key(step.id) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AgentTextField(
                        value = step.text,
                        onValueChange = { onStepChange(index, it) },
                        label = "Step ${index + 1}",
                        modifier = Modifier.weight(1f)
                    )
                    if (formState.steps.size > 1) {
                        TextButton(onClick = { onRemoveStep(index) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
        if (formState.canAddStep) {
            AppButton(
                text = "Add Step",
                onClick = onAddStep,
                style = AppButtonStyle.Outlined
            )
        } else {
            Text(
                text = "Maximum of ${CustomFlow.MAX_STEPS} steps reached.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Use menu numbers or tokens: phone, phone_confirm, amount. Example: 3, 1, phone, phone_confirm, amount",
            style = MaterialTheme.typography.bodySmall
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = formState.useDynamicPhone,
                onCheckedChange = onDynamicPhoneChange,
                colors = appFormCheckboxColors()
            )
            Text("Dynamic Phone Number", color = MaterialTheme.colorScheme.onSurface)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = formState.useDynamicAmount,
                onCheckedChange = onDynamicAmountChange,
                colors = appFormCheckboxColors()
            )
            Text("Dynamic Amount", color = MaterialTheme.colorScheme.onSurface)
        }
        AgentActionButton(text = "Save Custom Flow", onClick = onSave)
        AppButton(text = "Cancel", onClick = onCancel, style = AppButtonStyle.Outlined)
    }
}

@Composable
private fun CustomFlowCard(
    flow: CustomFlow,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val stepPreview = flow.normalizedSteps().joinToString(", ")
    AppCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(flow.name, style = MaterialTheme.typography.titleMedium)
            Text(
                if (stepPreview.isBlank()) {
                    "${flow.ussdCode} → direct dial"
                } else {
                    "${flow.ussdCode} → $stepPreview"
                },
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
