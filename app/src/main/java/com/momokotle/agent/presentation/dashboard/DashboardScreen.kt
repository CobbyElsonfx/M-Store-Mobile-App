package com.momokotle.agent.presentation.dashboard

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Store
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.ussd.CustomFlowExecutor
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.presentation.settings.SettingsHomeViewModel
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.ContactSearchSuggestions
import com.momokotle.agent.ui.components.DashboardGridButton
import com.momokotle.agent.ui.components.DashboardGridSpacer
import com.momokotle.agent.ui.components.PhoneNumberWithSaveRow
import com.momokotle.agent.ui.components.SaveContactDialog
import com.momokotle.agent.ui.theme.AppDimens

private data class GridAction(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
    val isSpacer: Boolean = false,
    val highlighted: Boolean = false,
    val isCustom: Boolean = false
)

@Composable
fun DashboardScreen(
    onCustomMenu: () -> Unit,
    onRunCustomMenu: (Long) -> Unit,
    savedStateHandle: SavedStateHandle? = null,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val contactUi by viewModel.contactUiState.collectAsStateWithLifecycle()
    val lastTx by viewModel.lastTransaction.collectAsStateWithLifecycle()
    val customFlows by viewModel.customFlows.collectAsStateWithLifecycle()
    val account by hiltViewModel<SettingsHomeViewModel>().account.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refreshPermissions()
        onPauseOrDispose { }
    }

    LaunchedEffect(savedStateHandle) {
        val handle = savedStateHandle ?: return@LaunchedEffect
        val phone = handle.get<String>("prefill_phone") ?: return@LaunchedEffect
        val networkName = handle.get<String>("prefill_network")
        val isPorted = handle.get<Boolean>("prefill_ported") ?: false
        val network = networkName?.let { runCatching { ContactNetwork.valueOf(it) }.getOrNull() }
            ?: ContactNetwork.UNKNOWN
        viewModel.applyPrefillContact(
            com.momokotle.agent.models.SavedContact(
                contactName = "",
                phoneNumber = phone,
                network = network,
                isPorted = isPorted
            )
        )
        handle.remove<String>("prefill_phone")
        handle.remove<String>("prefill_network")
        handle.remove<Boolean>("prefill_ported")
    }

    if (contactUi.showSaveDialog) {
        SaveContactDialog(
            formState = contactUi.saveDialogForm,
            onContactNameChange = viewModel::onSaveDialogNameChange,
            onPhoneNumberChange = viewModel::onSaveDialogPhoneChange,
            onNetworkChange = viewModel::onSaveDialogNetworkChange,
            onPortedChange = viewModel::onSaveDialogPortedChange,
            onDismiss = viewModel::dismissSaveDialog,
            onSave = viewModel::saveContactFromDialog,
            errorMessage = contactUi.saveDialogError,
            phoneEditable = true
        )
    }

    if (contactUi.showPostTransactionSavePrompt) {
        AlertDialog(
            onDismissRequest = viewModel::dismissPostTransactionSavePrompt,
            title = { Text("Save this number?") },
            text = { Text("Save ${contactUi.postTransactionPhone} as a contact for faster reuse.") },
            confirmButton = {
                TextButton(onClick = viewModel::acceptPostTransactionSave) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissPostTransactionSavePrompt) { Text("Not now") }
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.refreshPermissions()
    }

    val automationRunning = uiState.automationStatus == AutomationStatus.RUNNING ||
        uiState.automationStatus == AutomationStatus.WAITING_FOR_USER

    val actionsEnabled = !uiState.isLoading && !automationRunning

    fun runWithPermissions(action: () -> Unit) {
        when {
            !uiState.callPermissionGranted ->
                permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            !uiState.accessibilityEnabled ->
                viewModel.openAccessibilitySettings()
            else -> action()
        }
    }

    val gridActions = buildList {
        add(GridAction("cash_in", "CASH IN", Icons.Default.SouthWest, {
            runWithPermissions(viewModel::startCashIn)
        }))
        add(GridAction("cash_out", "CASH OUT", Icons.Default.NorthEast, {
            runWithPermissions(viewModel::startCashOut)
        }))
        add(GridAction("transfer", "TRANSFER", Icons.Default.Send, {
            runWithPermissions(viewModel::startTransferAirtime)
        }))
        add(GridAction("balance", "BALANCE", Icons.Default.AccountBalance, {
            runWithPermissions(viewModel::startBalance)
        }))
        add(GridAction("airtime", "AIRTIME", Icons.Default.SimCard, {
            runWithPermissions(viewModel::startTransferAirtime)
        }))
        add(GridAction("spacer", "", onClick = {}, isSpacer = true))
        add(GridAction("commission", "COMMISSION", Icons.Default.Payments, {
            runWithPermissions(viewModel::startCommission)
        }))
        add(GridAction("merchant", "PAY TO\nMERCHANT", Icons.Default.Store, {
            runWithPermissions(viewModel::startPayToMerchant)
        }))
        add(GridAction("evd", "EVD/\nJUST4U", Icons.Default.PhoneAndroid, {
            runWithPermissions(viewModel::startEvdJust4u)
        }))
        customFlows.forEach { flow ->
            add(
                customFlowGridAction(flow) {
                    runWithPermissions { onRunCustomMenu(flow.id) }
                }
            )
        }
        add(
            GridAction(
                id = "custom_menu",
                label = "CUSTOM\nMENU",
                icon = Icons.Default.Menu,
                onClick = { if (actionsEnabled) onCustomMenu() },
                highlighted = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(AppDimens.InputSpacing)
    ) {
        val greetingName = account?.fullName?.split(" ")?.firstOrNull().orEmpty()
        AppSectionHeader(
            title = if (greetingName.isNotBlank()) "Hello, $greetingName" else "Hello, Agent",
            subtitle = "Quick actions for MoMo transactions"
        )

        AutomationStatusCard(
            accessibilityEnabled = uiState.accessibilityEnabled,
            automationStatus = uiState.automationStatus,
            onEnableAccessibility = viewModel::openAccessibilitySettings
        )

        LastTransactionCard(
            transaction = lastTx,
            typeLabel = lastTx?.let { viewModel.typeLabel(it.type) },
            onRepeat = { viewModel.repeatLastTransaction() }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PhoneNumberWithSaveRow(
                    phoneValue = formState.phoneNumber,
                    onPhoneChange = viewModel::onPhoneChange,
                    onSaveContactClick = viewModel::openSaveContactDialog,
                    enabled = !uiState.isLoading
                )
                ContactSearchSuggestions(
                    contacts = contactUi.searchSuggestions,
                    onSelect = viewModel::selectContact
                )
            }
            AppTextField(
                value = formState.amount,
                onValueChange = viewModel::onAmountChange,
                label = "Amount",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formState.isPortedNumber,
                onCheckedChange = viewModel::onPortedNumberChange,
                enabled = !uiState.isLoading,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = "Mark if number is ported",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        uiState.errorMessage?.let { AppErrorBanner(it) }
        uiState.infoMessage?.let { AppInfoBanner(it) }

        if (automationRunning) {
            TextButton(onClick = viewModel::cancelAutomation) {
                Text("Cancel automation", color = MaterialTheme.colorScheme.error)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = true
        ) {
            items(items = gridActions, key = { it.id }) { action ->
                if (action.isSpacer) {
                    DashboardGridSpacer(modifier = Modifier.fillMaxWidth())
                } else {
                    DashboardGridButton(
                        label = action.label,
                        onClick = action.onClick,
                        enabled = actionsEnabled,
                        icon = action.icon,
                        highlighted = action.highlighted,
                        showCustomBadge = action.isCustom,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun customFlowGridAction(
    flow: CustomFlow,
    onClick: () -> Unit
): GridAction = GridAction(
    id = "custom_flow_${flow.id}",
    label = CustomFlowExecutor.gridLabel(flow.name),
    icon = Icons.Default.Apps,
    onClick = onClick,
    isCustom = true
)

@Composable
private fun AutomationStatusCard(
    accessibilityEnabled: Boolean,
    automationStatus: AutomationStatus,
    onEnableAccessibility: () -> Unit
) {
    val (statusText, tone) = when {
        !accessibilityEnabled -> "Accessibility off — enable to automate USSD" to AppStatusTone.Warning
        automationStatus == AutomationStatus.RUNNING -> "Automation running…" to AppStatusTone.Accent
        automationStatus == AutomationStatus.WAITING_FOR_USER -> "Waiting for PIN on USSD screen" to AppStatusTone.Accent
        automationStatus == AutomationStatus.SUCCESS -> "Last automation succeeded" to AppStatusTone.Success
        automationStatus == AutomationStatus.FAILED -> "Last automation failed" to AppStatusTone.Error
        else -> "Accessibility enabled — ready to automate USSD" to AppStatusTone.Success
    }
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Automation",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            AppStatusBadge(text = if (accessibilityEnabled) "ON" else "OFF", tone = tone)
        }
        if (!accessibilityEnabled) {
            TextButton(onClick = onEnableAccessibility, modifier = Modifier.padding(top = 4.dp)) {
                Text("Enable Accessibility", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun LastTransactionCard(
    transaction: Transaction?,
    typeLabel: String?,
    onRepeat: () -> Unit
) {
    AppCard {
        if (transaction == null) {
            Text(
                text = "No recent transaction",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Last transaction",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = transaction.phoneNumber ?: "—",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "GHS ${transaction.amount}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        typeLabel?.let {
                            AppStatusBadge(text = it, tone = AppStatusTone.Neutral)
                        }
                    }
                }
                TextButton(onClick = onRepeat) {
                    Text("Repeat", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
