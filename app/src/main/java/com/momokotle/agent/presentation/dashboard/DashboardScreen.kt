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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.ContactSearchSuggestions
import com.momokotle.agent.ui.components.DashboardGridButton
import com.momokotle.agent.ui.components.DashboardGridSpacer
import com.momokotle.agent.ui.components.PhoneNumberWithSaveRow
import com.momokotle.agent.ui.components.SaveContactDialog

private data class GridAction(
    val id: String,
    val label: String,
    val onClick: () -> Unit,
    val isSpacer: Boolean = false
)

@Composable
fun DashboardScreen(
    onCustomMenu: () -> Unit,
    savedStateHandle: SavedStateHandle? = null,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val contactUi by viewModel.contactUiState.collectAsStateWithLifecycle()
    val lastTx by viewModel.lastTransaction.collectAsStateWithLifecycle()

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
                TextButton(onClick = viewModel::acceptPostTransactionSave) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissPostTransactionSavePrompt) {
                    Text("Not now")
                }
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

    val gridActions = listOf(
        GridAction("cash_in", "CASH IN", onClick = {
            runWithPermissions(viewModel::startCashIn)
        }),
        GridAction("cash_out", "CASH OUT", onClick = {
            runWithPermissions(viewModel::startCashOut)
        }),
        GridAction("transfer", "TRANSFER", onClick = {
            runWithPermissions(viewModel::startTransferAirtime)
        }),
        GridAction("balance", "BALANCE", onClick = {
            runWithPermissions(viewModel::startBalance)
        }),
        GridAction("airtime", "AIRTIME", onClick = {
            runWithPermissions(viewModel::startTransferAirtime)
        }),
        GridAction("spacer", "", onClick = {}, isSpacer = true),
        GridAction("commission", "COMMISSION", onClick = {
            runWithPermissions(viewModel::startCommission)
        }),
        GridAction("merchant", "PAY TO\nMERCHANT", onClick = {
            runWithPermissions(viewModel::startPayToMerchant)
        }),
        GridAction("evd", "EVD/JUST4U", onClick = {
            runWithPermissions(viewModel::startEvdJust4u)
        }),
        GridAction("custom", "CUSTOM\nMENU", onClick = {
            if (actionsEnabled) onCustomMenu()
        })
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            AgentTextField(
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
                enabled = !uiState.isLoading
            )
            Text(
                text = "Mark this box if number is ported",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        uiState.infoMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!uiState.accessibilityEnabled) {
            TextButton(
                onClick = viewModel::openAccessibilitySettings,
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Enable Accessibility",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

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
            userScrollEnabled = false
        ) {
            items(
                items = gridActions,
                key = { it.id }
            ) { action ->
                if (action.isSpacer) {
                    DashboardGridSpacer(modifier = Modifier.fillMaxWidth())
                } else {
                    DashboardGridButton(
                        label = action.label,
                        onClick = action.onClick,
                        enabled = actionsEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (transaction == null) {
                Text(
                    text = "No recent transaction",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Last transaction",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = transaction.phoneNumber ?: "—",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "GHS ${transaction.amount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = typeLabel.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                TextButton(onClick = onRepeat) {
                    Text("Repeat", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
