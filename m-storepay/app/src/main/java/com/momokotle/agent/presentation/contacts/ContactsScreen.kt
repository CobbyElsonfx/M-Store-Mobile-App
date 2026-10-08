package com.momokotle.agent.presentation.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSearchField
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.SaveContactDialog
import com.momokotle.agent.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onBack: () -> Unit,
    onUseContact: (SavedContact) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackMessage) {
        uiState.snackMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackMessage()
        }
    }

    if (uiState.showAddDialog) {
        SaveContactDialog(
            formState = uiState.dialogForm,
            onContactNameChange = viewModel::onDialogNameChange,
            onPhoneNumberChange = viewModel::onDialogPhoneChange,
            onNetworkChange = viewModel::onDialogNetworkChange,
            onPortedChange = viewModel::onDialogPortedChange,
            onDismiss = viewModel::dismissDialog,
            onSave = viewModel::saveFromDialog,
            errorMessage = uiState.dialogError
        )
    }

    AppScaffold(
        title = "Saved Contacts",
        onBack = onBack,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::openAddDialog,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add contact")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AgentTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchChange,
                label = "Search contacts"
            )

            if (uiState.filteredContacts.isEmpty()) {
                AppEmptyState(
                    title = if (uiState.searchQuery.isBlank()) "No saved contacts" else "No matches",
                    message = if (uiState.searchQuery.isBlank()) {
                        "Tap + to save a number for faster USSD."
                    } else {
                        "Try a different name or phone number."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredContacts, key = { it.id }) { contact ->
                        ContactListItem(
                            contact = contact,
                            onUse = { onUseContact(contact) },
                            onDelete = { viewModel.deleteContact(contact.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactListItem(
    contact: SavedContact,
    onUse: () -> Unit,
    onDelete: () -> Unit
) {
    AppCard(onClick = onUse) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.contactName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildString {
                        append(contact.network.displayName)
                        if (contact.isPorted) append(" · Ported")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete contact",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
