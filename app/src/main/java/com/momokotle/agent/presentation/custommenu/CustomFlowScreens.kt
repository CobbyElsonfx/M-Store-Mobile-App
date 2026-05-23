package com.momokotle.agent.presentation.custommenu

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.ErrorBanner
import com.momokotle.agent.ui.components.InfoBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFlowListScreen(
    onBack: () -> Unit,
    viewModel: CustomFlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()
    val flows by viewModel.customFlows.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.refreshPermissions() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Menu") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Phone & amount for dynamic steps (phone, amount)",
                    style = MaterialTheme.typography.bodySmall
                )
                AgentTextField(
                    value = formState.phoneNumber,
                    onValueChange = viewModel::onPhoneChange,
                    label = "Phone Number"
                )
                AgentTextField(
                    value = formState.amount,
                    onValueChange = viewModel::onAmountChange,
                    label = "Amount (GHS)"
                )

                uiState.errorMessage?.let { ErrorBanner(it) }
                uiState.infoMessage?.let { InfoBanner(it) }

                AgentActionButton(text = "Add Custom Flow", onClick = viewModel::startNewFlow)

                if (flows.isEmpty()) {
                    Text("No custom flows yet", modifier = Modifier.padding(top = 16.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(flows, key = { it.id }) { flow ->
                            CustomFlowCard(
                                flow = flow,
                                onRun = {
                                    if (!uiState.callPermissionGranted) {
                                        permissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                    } else if (!uiState.accessibilityEnabled) {
                                        viewModel.openAccessibilitySettings()
                                    } else {
                                        viewModel.runFlow(flow)
                                    }
                                },
                                onEdit = { viewModel.editFlow(flow) },
                                onDelete = { viewModel.deleteFlow(flow.id) }
                            )
                        }
                    }
                }

                val running = uiState.automationStatus == AutomationStatus.RUNNING ||
                    uiState.automationStatus == AutomationStatus.WAITING_FOR_USER
                if (running) {
                    AgentActionButton(text = "Cancel Automation", onClick = viewModel::cancelAutomation)
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
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(flow.name, style = MaterialTheme.typography.titleMedium)
            Text("${flow.ussdCode} → ${flow.step1}, ${flow.step2}, ${flow.step3}".trimEnd(',', ' '))
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
