package com.momokotle.agent.presentation.settings

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.BuildConfig
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.models.UserAccount
import com.momokotle.agent.ui.components.AgentActionButton

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
    viewModel: SettingsHomeViewModel = hiltViewModel()
) {
    val account by viewModel.account.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileCard(account = account)

            SettingsSection(title = "Your Profile") {
                SettingsMenuItem(title = "Account Profile", onClick = onAccountProfile)
            }

            SettingsSection(title = "Profit Calculator") {
                SettingsMenuItem(title = "Calculation", onClick = onCalculation)
                SettingsMenuItem(title = "Cash In Settings", onClick = onCashInSettings)
                SettingsMenuItem(title = "Cash Out Settings", onClick = onCashOutSettings)
            }

            SettingsSection(title = "Options") {
                SettingsMenuItem(title = "App Settings", onClick = onAppSettings)
                SettingsMenuItem(title = "Saved Contacts", onClick = onSavedContacts)
                SettingsMenuItem(title = "Contact Support", onClick = onContactSupport)
            }

            SettingsSection(title = "About") {
                SettingsMenuItem(
                    title = stringResource(com.momokotle.agent.R.string.about_title),
                    onClick = onAbout
                )
                SettingsMenuItem(title = "Share App", onClick = viewModel::shareApp)
            }
        }
    }
}

@Composable
private fun ProfileCard(account: UserAccount?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountProfileScreen(
    onBack: () -> Unit,
    viewModel: AccountProfileViewModel = hiltViewModel()
) {
    val account by viewModel.account.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileDetailRow(label = "Full name", value = account?.fullName)
            ProfileDetailRow(label = "Email", value = account?.email)
            account?.phoneNumber?.let {
                ProfileDetailRow(label = "Phone number", value = it)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    onBack: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsToggleRow(
                label = "Vibrate on Touch",
                checked = uiState.settings.vibrateOnTouch,
                onCheckedChange = viewModel::setVibrateOnTouch
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Accessibility Service", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (uiState.accessibilityEnabled) "Enabled" else "Disabled",
                    color = if (uiState.accessibilityEnabled) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                if (!uiState.accessibilityEnabled) {
                    AgentActionButton(
                        text = "Open Accessibility Settings",
                        onClick = viewModel::openAccessibilitySettings
                    )
                }
            }

            SettingsToggleRow(
                label = "Transaction Verification",
                checked = uiState.settings.transactionVerificationEnabled,
                onCheckedChange = viewModel::setTransactionVerification
            )

            Text(text = "SIM slot selection", style = MaterialTheme.typography.titleMedium)
            SimSlotSelector(
                label = "MTN Mobile Money",
                selected = uiState.settings.simMtnMomo,
                onSelect = { viewModel.setSimSlot(SimPurpose.MTN_MOBILE_MONEY, it) }
            )
            SimSlotSelector(
                label = "Telecel Cash",
                selected = uiState.settings.simTelecelCash,
                onSelect = { viewModel.setSimSlot(SimPurpose.TELECEL_CASH, it) }
            )
            SimSlotSelector(
                label = "AirtelTigo Cash",
                selected = uiState.settings.simAirtelTigoCash,
                onSelect = { viewModel.setSimSlot(SimPurpose.AIRTELTIGO_CASH, it) }
            )

            Text(text = "Airtime Transfer Management", style = MaterialTheme.typography.titleMedium)
            SimSlotSelector(
                label = "MTN Airtime Transfer",
                selected = uiState.settings.simMtnAirtimeTransfer,
                onSelect = { viewModel.setSimSlot(SimPurpose.MTN_AIRTIME_TRANSFER, it) }
            )
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
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SimSlotSelector(
    label: String,
    selected: SimSlot,
    onSelect: (SimSlot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SimSlot.entries.forEach { slot ->
                FilterChip(
                    selected = selected == slot,
                    onClick = { onSelect(slot) },
                    label = {
                        Text(
                            when (slot) {
                                SimSlot.SIM1 -> "SIM 1"
                                SimSlot.SIM2 -> "SIM 2"
                                SimSlot.NONE -> "None"
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Support") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileDetailRow(
                label = "Email",
                value = stringResource(com.momokotle.agent.R.string.support_email)
            )
            ProfileDetailRow(
                label = "Phone",
                value = stringResource(com.momokotle.agent.R.string.support_phone)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(com.momokotle.agent.R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileDetailRow(
                label = "App name",
                value = stringResource(com.momokotle.agent.R.string.app_name)
            )
            ProfileDetailRow(label = "Version", value = BuildConfig.VERSION_NAME)
        }
    }
}
