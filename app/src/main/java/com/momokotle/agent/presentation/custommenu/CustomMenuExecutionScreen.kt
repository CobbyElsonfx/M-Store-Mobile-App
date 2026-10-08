package com.momokotle.agent.presentation.custommenu

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.CustomMenuType
import com.momokotle.agent.presentation.common.UssdFlowScreenContent
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun CustomMenuExecutionScreen(
    flowId: Long,
    onBack: () -> Unit,
    viewModel: CustomFlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val execState by viewModel.executionState.collectAsStateWithLifecycle()

    LaunchedEffect(flowId) {
        viewModel.loadForExecution(flowId)
    }

    LifecycleResumeEffect(Unit) {
        viewModel.refreshPermissions()
        onPauseOrDispose { }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.refreshPermissions() }

    AppScaffold(
        title = execState.flow?.name ?: "Run Menu",
        onBack = onBack
    ) { padding ->
        when {
            execState.loadError != null -> {
                AppErrorBanner(
                    message = execState.loadError!!,
                    modifier = Modifier.padding(padding).padding(AppDimens.ScreenHorizontal)
                )
            }
            execState.flow == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Loading…",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(AppDimens.ScreenHorizontal)
                    )
                }
            }
            else -> {
                val flow = execState.flow!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
                ) {
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "USSD code",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = flow.ussdCode,
                                style = MaterialTheme.typography.titleMedium
                            )
                            AppStatusBadge(
                                text = when (execState.menuType) {
                                    CustomMenuType.STANDARD -> "Standard (step automation)"
                                    CustomMenuType.DIRECT -> "Direct dial"
                                    null -> "Custom"
                                },
                                tone = AppStatusTone.Neutral
                            )
                            if (execState.menuType == CustomMenuType.STANDARD) {
                                val steps = listOf(flow.step1, flow.step2, flow.step3)
                                    .filter { it.isNotBlank() }
                                    .joinToString(" → ")
                                if (steps.isNotBlank()) {
                                    Text(
                                        text = "Steps: $steps",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    UssdFlowScreenContent(
                        uiState = uiState,
                        primaryLabel = "Run",
                        onPrimaryClick = {
                            when {
                                !uiState.callPermissionGranted ->
                                    permissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                !uiState.accessibilityEnabled ->
                                    viewModel.openAccessibilitySettings()
                                else -> viewModel.runExecution()
                            }
                        },
                        onCancel = viewModel::cancelAutomation,
                        onOpenAccessibility = viewModel::openAccessibilitySettings,
                        formContent = {
                            if (execState.requiresPhone) {
                                AgentTextField(
                                    value = execState.phoneNumber,
                                    onValueChange = viewModel::onExecutionPhoneChange,
                                    label = "Phone Number"
                                )
                            }
                            if (execState.requiresAmount) {
                                AgentTextField(
                                    value = execState.amount,
                                    onValueChange = viewModel::onExecutionAmountChange,
                                    label = "Amount (GHS)"
                                )
                            }
                            if (!execState.requiresPhone && !execState.requiresAmount) {
                                Text(
                                    text = "No phone or amount required. Tap Run to start.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
