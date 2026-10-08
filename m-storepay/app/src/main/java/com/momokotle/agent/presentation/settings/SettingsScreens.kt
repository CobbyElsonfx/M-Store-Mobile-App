package com.momokotle.agent.presentation.settings

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.BuildConfig
import com.momokotle.agent.models.AutomationSpeed
import com.momokotle.agent.models.DuplicateTransactionWarningWindow
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.momokotle.agent.models.UserAccount
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppDetailRow
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppMenuListItem
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.theme.AppDimens
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsHomeScreen(
    onAccountProfile: () -> Unit,
    onAppSettings: () -> Unit,
    onCalculation: () -> Unit,
    onCashInSettings: () -> Unit,
    onCashOutSettings: () -> Unit,
    onSavedContacts: () -> Unit,
    onContactSupport: () -> Unit,
    onAbout: () -> Unit,
    onReferral: () -> Unit,
    onWallet: () -> Unit = {},
    onSubscription: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onMySubmissions: () -> Unit,
    onMyOrders: () -> Unit = {},
    viewModel: SettingsHomeViewModel = hiltViewModel()
) {
    val account by viewModel.account.collectAsStateWithLifecycle()
    val isLoggingOut by viewModel.isLoggingOut.collectAsStateWithLifecycle()

    AppScaffold(title = "Settings") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileCard(account = account)

            SettingsSection(title = "Your profile") {
                SettingsMenuItem(title = "Account Profile", subtitle = "Name, phone, and status", onClick = onAccountProfile)
                SettingsMenuItem(title = "My Orders", subtitle = "Store orders and delivery", onClick = onMyOrders)
                SettingsMenuItem(title = "My Submissions", subtitle = "Registration forms", onClick = onMySubmissions)
            }

            SettingsSection(title = "Commission") {
                SettingsMenuItem(title = "Preview", subtitle = "Estimate cash in and cash out", onClick = onCalculation)
                SettingsMenuItem(title = "Cash In Settings", subtitle = "Percentage, fixed, or tiers", onClick = onCashInSettings)
                SettingsMenuItem(title = "Cash Out Settings", subtitle = "Percentage, fixed, or tiers", onClick = onCashOutSettings)
            }

            SettingsSection(title = "Business") {
                SettingsMenuItem(title = "Referral Center", subtitle = "Share your code and earnings", onClick = onReferral)
                SettingsMenuItem(title = "Wallet", subtitle = "Balances and withdrawals", onClick = onWallet)
                SettingsMenuItem(title = "Subscription", subtitle = "Plans and access", onClick = onSubscription)
                SettingsMenuItem(title = "Notifications", subtitle = "Orders, commissions, and alerts", onClick = onNotifications)
            }

            SettingsSection(title = "Options") {
                SettingsMenuItem(title = "App Settings", subtitle = "Accessibility, SIM, and alerts", onClick = onAppSettings)
                SettingsMenuItem(title = "Saved Contacts", subtitle = "Reuse numbers for USSD", onClick = onSavedContacts)
                SettingsMenuItem(
                    title = "Help Center",
                    subtitle = "Email, phone, and WhatsApp support",
                    onClick = onContactSupport
                )
            }

            SettingsSection(title = "About") {
                SettingsMenuItem(
                    title = stringResource(com.momokotle.agent.R.string.about_title),
                    subtitle = "Version and privacy",
                    onClick = onAbout
                )
                SettingsMenuItem(title = "Share App", subtitle = "Invite another agent", onClick = viewModel::shareApp)
            }

            SettingsSection(title = "Account") {
                SettingsMenuItem(
                    title = if (isLoggingOut) "Logging out…" else "Logout",
                    subtitle = "Sign out of this device",
                    destructive = true,
                    onClick = { if (!isLoggingOut) viewModel.logout() }
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(account: UserAccount?) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    val initials = account?.fullName
                        ?.split(" ")
                        ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        ?.take(2)
                        ?.joinToString("")
                        .orEmpty()
                        .ifBlank { "?" }
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = account?.fullName ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = account?.email ?: "—",
                    style = MaterialTheme.typography.bodyMedium,
                    color = com.momokotle.agent.ui.theme.AppFormStyles.label
                )
                account?.phoneNumber?.takeIf { it.isNotBlank() }?.let { phone ->
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.momokotle.agent.ui.theme.AppFormStyles.label
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AppSectionHeader(title = title)
        AppCard {
            Column { content() }
        }
    }
}

@Composable
private fun SettingsMenuItem(
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
    destructive: Boolean = false
) {
    AppMenuListItem(
        title = title,
        subtitle = subtitle,
        onClick = onClick,
        titleColor = if (destructive) {
            MaterialTheme.colorScheme.error
        } else {
            com.momokotle.agent.ui.theme.AppFormStyles.onSurface
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountProfileScreen(
    onBack: () -> Unit,
    viewModel: AccountProfileViewModel = hiltViewModel()
) {
    val account by viewModel.account.collectAsStateWithLifecycle()
    val initials = account?.fullName
        ?.split(" ")
        ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
        ?.take(2)
        ?.joinToString("")
        .orEmpty()
        .ifBlank { "?" }
    val status = account?.accountStatus.orEmpty()
    val statusTone = when {
        status.equals("Active", ignoreCase = true) -> AppStatusTone.Success
        status.equals("Suspended", ignoreCase = true) -> AppStatusTone.Error
        status.isBlank() -> AppStatusTone.Neutral
        else -> AppStatusTone.Warning
    }

    AppScaffold(title = "Profile", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            AppCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = initials,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = account?.fullName?.ifBlank { "Agent account" } ?: "Agent account",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = account?.phoneNumber?.ifBlank { account?.email }.orEmpty()
                                .ifBlank { "No contact on file" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (status.isNotBlank()) {
                            AppStatusBadge(
                                text = status,
                                tone = statusTone
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppSectionHeader(title = "Personal information")
                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppDetailRow(label = "Full name", value = account?.fullName)
                        AppDetailRow(label = "Phone", value = account?.phoneNumber)
                        AppDetailRow(label = "Email", value = account?.email)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppSectionHeader(title = "Account information")
                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppDetailRow(label = "Location", value = account?.location)
                        AppDetailRow(label = "Account status", value = account?.accountStatus)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value ?: "—",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppSettingsScreen(
    onBack: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val phoneStateLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* selection already saved; permission enables dial-time SIM routing */ }

    fun selectSim(purpose: SimPurpose, slot: SimSlot) {
        viewModel.setSimSlot(purpose, slot)
        if (slot != SimSlot.NONE) {
            phoneStateLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }
    }

    LifecycleResumeEffect(Unit) {
        viewModel.refreshAccessibility()
        onPauseOrDispose { }
    }

    AppScaffold(title = "App Settings", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSectionHeader(
                        title = "Preferences",
                        subtitle = "Haptics and transaction checks"
                    )
                    SettingsToggleRow(
                        label = "Vibrate on touch",
                        checked = uiState.settings.vibrateOnTouch,
                        onCheckedChange = viewModel::setVibrateOnTouch
                    )
                    SettingsToggleRow(
                        label = "Transaction verification",
                        checked = uiState.settings.transactionVerificationEnabled,
                        onCheckedChange = viewModel::setTransactionVerification
                    )
                }
            }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppSectionHeader(
                        title = "Duplicate transaction warning",
                        subtitle = "Warn before dialing if the same number was used recently. " +
                            "Current: ${uiState.settings.duplicateTransactionWarningWindow.displayName}"
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DuplicateTransactionWarningWindow.entries.forEach { window ->
                            AppFilterChip(
                                label = window.displayName,
                                selected = uiState.settings.duplicateTransactionWarningWindow == window,
                                onClick = {
                                    viewModel.setDuplicateTransactionWarningWindow(window)
                                }
                            )
                        }
                    }
                }
            }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppSectionHeader(
                        title = "USSD automation speed",
                        subtitle = uiState.settings.automationSpeed.description
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            AutomationSpeed.SAFE,
                            AutomationSpeed.NORMAL,
                            AutomationSpeed.FAST
                        ).forEach { speed ->
                            AppFilterChip(
                                label = speed.displayName,
                                selected = uiState.settings.automationSpeed == speed,
                                onClick = { viewModel.setAutomationSpeed(speed) }
                            )
                        }
                    }
                }
            }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSectionHeader(
                        title = "Accessibility",
                        subtitle = "Required for USSD automation"
                    )
                    AppStatusBadge(
                        text = if (uiState.accessibilityEnabled) "Enabled" else "Disabled",
                        tone = if (uiState.accessibilityEnabled) {
                            AppStatusTone.Success
                        } else {
                            AppStatusTone.Warning
                        }
                    )
                    if (!uiState.accessibilityEnabled) {
                        AppButton(
                            text = "Open Accessibility Settings",
                            onClick = viewModel::openAccessibilitySettings,
                            style = AppButtonStyle.Accent
                        )
                    }
                }
            }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSectionHeader(
                        title = "SIM slots",
                        subtitle = "Choose which SIM dials each network (avoids the system SIM picker)"
                    )
                    SimSlotSelector(
                        label = "MTN Mobile Money",
                        selected = uiState.settings.simMtnMomo,
                        onSelect = { selectSim(SimPurpose.MTN_MOBILE_MONEY, it) }
                    )
                    SimSlotSelector(
                        label = "Telecel Cash",
                        selected = uiState.settings.simTelecelCash,
                        onSelect = { selectSim(SimPurpose.TELECEL_CASH, it) }
                    )
                    SimSlotSelector(
                        label = "AirtelTigo Cash",
                        selected = uiState.settings.simAirtelTigoCash,
                        onSelect = { selectSim(SimPurpose.AIRTELTIGO_CASH, it) }
                    )
                    SimSlotSelector(
                        label = "MTN Airtime Transfer",
                        selected = uiState.settings.simMtnAirtimeTransfer,
                        onSelect = { selectSim(SimPurpose.MTN_AIRTIME_TRANSFER, it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = com.momokotle.agent.ui.theme.AppFormStyles.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = com.momokotle.agent.ui.theme.AppFormStyles.onPrimary,
                checkedTrackColor = com.momokotle.agent.ui.theme.AppFormStyles.primary,
                uncheckedThumbColor = com.momokotle.agent.ui.theme.AppFormStyles.onSurface,
                uncheckedTrackColor = com.momokotle.agent.ui.theme.AppFormStyles.surface
            )
        )
    }
}

@Composable
private fun SimSlotSelector(
    label: String,
    selected: SimSlot,
    onSelect: (SimSlot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = com.momokotle.agent.ui.theme.AppFormStyles.onSurface
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SimSlot.entries.forEach { slot ->
                val label = when (slot) {
                    SimSlot.SIM1 -> "SIM 1"
                    SimSlot.SIM2 -> "SIM 2"
                    SimSlot.NONE -> "None"
                }
                AppFilterChip(
                    label = label,
                    selected = selected == slot,
                    onClick = { onSelect(slot) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(
    onBack: () -> Unit,
    viewModel: HelpCenterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actionError by viewModel.actionError.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.clearActionError()
    }

    AppScaffold(title = "Help Center", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppSectionHeader(
                title = "We can help",
                subtitle = "Reach support by email, phone, or WhatsApp during business hours."
            )

            actionError?.let { AppErrorBanner(it) }

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppDetailRow(
                        label = "Email",
                        value = stringResource(com.momokotle.agent.R.string.support_email)
                    )
                    AppDetailRow(
                        label = "Phone",
                        value = stringResource(com.momokotle.agent.R.string.support_phone)
                    )
                }
            }

            AppSectionHeader(
                title = "WhatsApp",
                subtitle = "Message an admin directly in WhatsApp."
            )

            when {
                uiState.loading -> {
                    AppInfoBanner("Loading support options…")
                }
                uiState.hasWhatsAppSupport -> {
                    AppButton(
                        text = "Chat with Admin on WhatsApp",
                        onClick = { viewModel.openWhatsAppChat(context) }
                    )
                }
                else -> {
                    AppErrorBanner("WhatsApp support is not configured yet. Please try again later.")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val privacyUrl = stringResource(com.momokotle.agent.R.string.privacy_policy_url)
    AppScaffold(
        title = stringResource(com.momokotle.agent.R.string.about_title),
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppDetailRow(
                        label = "App name",
                        value = stringResource(com.momokotle.agent.R.string.app_name)
                    )
                    AppDetailRow(label = "Version", value = BuildConfig.VERSION_NAME)
                }
            }
            AppButton(
                text = stringResource(com.momokotle.agent.R.string.privacy_policy_label),
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)))
                },
                style = AppButtonStyle.Outlined
            )
        }
    }
}
