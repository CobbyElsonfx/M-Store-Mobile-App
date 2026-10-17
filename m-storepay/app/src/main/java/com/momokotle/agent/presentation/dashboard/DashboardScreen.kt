package com.momokotle.agent.presentation.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Store
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.ussd.CustomFlowExecutor
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.momokotle.agent.domain.ussd.UssdAccessResult
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.CommissionSummary
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.presentation.settings.SettingsHomeViewModel
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.ContactSearchSuggestions
import com.momokotle.agent.ui.components.DashboardGridButton
import com.momokotle.agent.ui.components.PhoneNumberWithClearRow
import com.momokotle.agent.ui.components.DuplicateTransactionWarningDialog
import com.momokotle.agent.ui.components.LargeTransactionConfirmDialog
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.appFormCheckboxColors
import com.momokotle.agent.utils.PermissionHelper

private data class GridAction(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
    val isSpacer: Boolean = false,
    val highlighted: Boolean = false,
)

@Composable
fun DashboardScreen(
    onCustomMenu: () -> Unit,
    onRunCustomMenu: (Long) -> Unit,
    onSubscribe: () -> Unit = {},
    savedStateHandle: SavedStateHandle? = null,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val contactUi by viewModel.contactUiState.collectAsStateWithLifecycle()
    val lastTx by viewModel.lastTransaction.collectAsStateWithLifecycle()
    val todayCommission by viewModel.todayCommissionSummary.collectAsStateWithLifecycle()
    val customFlows by viewModel.customFlows.collectAsStateWithLifecycle()
    val largeTxConfirm by viewModel.largeTransactionConfirm.collectAsStateWithLifecycle()
    val duplicateTxConfirm by viewModel.duplicateTransactionConfirm.collectAsStateWithLifecycle()
    val account by hiltViewModel<SettingsHomeViewModel>().account.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refreshPermissions()
        viewModel.refreshUssdFlows()
        viewModel.refreshSubscriptionFromServer()
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

    largeTxConfirm?.let { pending ->
        LargeTransactionConfirmDialog(
            amount = pending.amount,
            phoneNumber = pending.phoneNumber.ifBlank { null },
            onConfirm = viewModel::confirmLargeTransaction,
            onDismiss = viewModel::dismissLargeTransactionConfirm
        )
    }

    duplicateTxConfirm?.let { pending ->
        DuplicateTransactionWarningDialog(
            prior = pending.prior,
            onConfirm = viewModel::confirmDuplicateTransaction,
            onDismiss = viewModel::dismissDuplicateTransactionConfirm
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.refreshPermissions()
    }

    val automationRunning = uiState.automationStatus.isActive

    val actionsEnabled = !uiState.isLoading && !automationRunning
    val ussdLocked = uiState.subscriptionAccess != UssdAccessResult.ALLOWED
    val context = androidx.compose.ui.platform.LocalContext.current

    fun runWithPermissions(requireSms: Boolean = false, action: () -> Unit) {
        if (ussdLocked) {
            viewModel.showSubscriptionBlocked()
            onSubscribe()
            return
        }
        when {
            !uiState.callPermissionGranted ->
                permissionLauncher.launch(
                    if (requireSms) PermissionHelper.cashOutPermissions
                    else PermissionHelper.dialPermissions
                )
            requireSms && !PermissionHelper.hasSmsPermission(context) ->
                permissionLauncher.launch(PermissionHelper.smsPermissions)
            !uiState.accessibilityEnabled ->
                viewModel.openAccessibilitySettings()
            else -> action()
        }
    }

    val featuredActions = listOf(
        GridAction(
            id = "cash_in",
            label = "CASH IN",
            icon = Icons.Default.SouthWest,
            onClick = { runWithPermissions { viewModel.startCashIn() } },
            highlighted = true
        ),
        GridAction(
            id = "cash_out",
            label = "CASH OUT",
            icon = Icons.Default.NorthEast,
            onClick = { runWithPermissions(requireSms = true) { viewModel.startCashOut() } },
            highlighted = true
        )
    )
    val gridActions = buildList {
        add(GridAction("balance", "BALANCE", Icons.Default.AccountBalance, {
            runWithPermissions { viewModel.startBalance() }
        }))
        add(GridAction("airtime", "AIRTIME", Icons.Default.SimCard, {
            runWithPermissions { viewModel.startTransferAirtime() }
        }))
        add(GridAction("commission", "COMMISSION", Icons.Default.Payments, {
            runWithPermissions { viewModel.startCommission() }
        }))
        add(GridAction("merchant", "MERCHANT", Icons.Default.Store, {
            runWithPermissions { viewModel.startPayToMerchant() }
        }))
        add(GridAction("evd", "EVD/J4U", Icons.Default.PhoneAndroid, {
            runWithPermissions { viewModel.startEvdJust4u() }
        }))
        customFlows.forEach { flow ->
            add(
                customFlowGridAction(flow) {
                    // Use dashboard phone/amount fields and dial — no separate Run screen.
                    runWithPermissions { viewModel.runCustomFlowFromDashboard(flow.id) }
                }
            )
        }
        add(
            GridAction(
                id = "custom_menu",
                label = "CUSTOM",
                icon = Icons.Default.Menu,
                onClick = { if (actionsEnabled) onCustomMenu() },
                highlighted = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val greetingName = account?.fullName?.split(" ")?.firstOrNull().orEmpty()
            Text(
                text = if (greetingName.isNotBlank()) "Hello, $greetingName" else "Hello, Agent",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            TodayCommissionCard(summary = todayCommission)

            AutomationStatusCard(
                accessibilityEnabled = uiState.accessibilityEnabled,
                automationStatus = uiState.automationStatus,
                subscriptionAccess = uiState.subscriptionAccess,
                onEnableAccessibility = viewModel::openAccessibilitySettings,
                onSubscribe = onSubscribe
            )

            LastTransactionCard(
                transaction = lastTx,
                typeLabel = lastTx?.let { viewModel.typeLabel(it.type) },
                onRepeat = { viewModel.repeatLastTransaction() }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    PhoneNumberWithClearRow(
                        phoneValue = formState.phoneNumber,
                        onPhoneChange = viewModel::onPhoneChange,
                        onClearFieldsClick = viewModel::clearFormFields,
                        clearEnabled = formState.phoneNumber.isNotBlank() ||
                            formState.amount.isNotBlank(),
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    colors = appFormCheckboxColors()
                )
                Text(
                    text = "Ported number",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            uiState.errorMessage?.let { AppErrorBanner(it) }
            uiState.infoMessage?.let { AppInfoBanner(it) }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        Text(
            text = "Quick actions",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DashboardActionGrid(
            featuredActions = featuredActions,
            actions = gridActions,
            actionsEnabled = actionsEnabled,
            locked = ussdLocked
        )
    }
}

private fun customFlowGridAction(
    flow: CustomFlow,
    onClick: () -> Unit
): GridAction = GridAction(
    id = "custom_flow_${flow.id}",
    label = CustomFlowExecutor.gridLabel(flow.name),
    icon = Icons.Default.Apps,
    onClick = onClick
)

@Composable
private fun DashboardActionGrid(
    featuredActions: List<GridAction>,
    actions: List<GridAction>,
    actionsEnabled: Boolean,
    locked: Boolean,
    modifier: Modifier = Modifier
) {
    val secondaryActions = actions.filterNot { it.isSpacer }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.DashboardGridSpacing)
    ) {
        if (featuredActions.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.DashboardGridSpacing)
            ) {
                featuredActions.forEach { action ->
                    DashboardGridButton(
                        label = action.label,
                        onClick = action.onClick,
                        enabled = actionsEnabled,
                        icon = action.icon,
                        highlighted = action.highlighted,
                        compact = false,
                        featured = true,
                        locked = locked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        secondaryActions.chunked(4).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.DashboardGridSpacing)
            ) {
                rowActions.forEach { action ->
                    DashboardGridButton(
                        label = action.label,
                        onClick = action.onClick,
                        enabled = actionsEnabled,
                        icon = action.icon,
                        highlighted = action.highlighted,
                        locked = locked && action.id != "custom_menu",
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(4 - rowActions.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TodayCommissionCard(summary: CommissionSummary) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DayTotalColumn(
                    label = "Cash In",
                    amount = summary.cashInTotal,
                    modifier = Modifier.weight(1f)
                )
                DayTotalColumn(
                    label = "Cash Out",
                    amount = summary.cashOutTotal,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Commission",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "GHS ${"%.2f".format(summary.totalCommission)}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun DayTotalColumn(
    label: String,
    amount: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "GHS ${"%.2f".format(amount)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AutomationStatusCard(
    accessibilityEnabled: Boolean,
    automationStatus: AutomationStatus,
    subscriptionAccess: UssdAccessResult,
    onEnableAccessibility: () -> Unit,
    onSubscribe: () -> Unit
) {
    val (statusText, tone) = when {
        !accessibilityEnabled -> "Accessibility off" to AppStatusTone.Warning
        automationStatus == AutomationStatus.RUNNING ||
        automationStatus == AutomationStatus.DIALING ||
        automationStatus == AutomationStatus.STARTING -> "Running…" to AppStatusTone.Accent
        automationStatus == AutomationStatus.WAITING_FOR_PIN -> "Waiting for PIN" to AppStatusTone.Accent
        automationStatus == AutomationStatus.SUCCESS -> "Last run succeeded" to AppStatusTone.Success
        automationStatus == AutomationStatus.FAILED -> "Last run failed" to AppStatusTone.Error
        else -> "Ready to automate" to AppStatusTone.Success
    }
    val ussdLine = when (subscriptionAccess) {
        UssdAccessResult.ALLOWED -> "USSD automation · Active"
        UssdAccessResult.SUBSCRIPTION_EXPIRED -> "USSD automation · Subscription expired"
        UssdAccessResult.SUBSCRIPTION_REQUIRED -> "USSD automation · Subscription required"
        UssdAccessResult.SUBSCRIPTION_STATE_UNKNOWN -> "USSD automation · Verify subscription"
    }
    AppCard(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = when (tone) {
                    AppStatusTone.Success -> AppColors.Success
                    AppStatusTone.Warning -> AppColors.Warning
                    AppStatusTone.Error -> AppColors.Error
                    else -> MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            AppStatusBadge(
                text = if (accessibilityEnabled) "ON" else "OFF",
                tone = if (accessibilityEnabled) AppStatusTone.Success else AppStatusTone.Warning
            )
            if (!accessibilityEnabled) {
                CompactStatusAction(
                    text = "Enable",
                    onClick = onEnableAccessibility
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ussdLine,
                style = MaterialTheme.typography.labelSmall,
                color = if (subscriptionAccess == UssdAccessResult.ALLOWED) {
                    AppColors.Success
                } else {
                    AppColors.Warning
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (subscriptionAccess != UssdAccessResult.ALLOWED) {
                CompactStatusAction(
                    text = subscriptionAccess.actionLabel,
                    onClick = onSubscribe
                )
            }
        }
    }
}

@Composable
private fun CompactStatusAction(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
        modifier = Modifier
            .height(28.dp)
            .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
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
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        append(transaction.phoneNumber ?: "—")
                        append(" · GHS ${transaction.amount}")
                        typeLabel?.let { append(" · $it") }
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onRepeat) {
                    Text("Repeat", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
