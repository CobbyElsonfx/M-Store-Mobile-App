package com.momokotle.agent.presentation.store

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreItem
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.ErrorBanner
import com.momokotle.agent.ui.components.InfoBanner
import com.momokotle.agent.ui.components.InternetRequiredBanner
import com.momokotle.agent.utils.InternetMessages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(viewModel: StoreViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.refreshConnectivity() }

    Scaffold(topBar = { TopAppBar(title = { Text("Store") }) }) { padding ->
        if (!uiState.isOnline) {
            InternetRequiredBanner(
                message = InternetMessages.STORE_REQUIRES_INTERNET,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            uiState.message?.let { InfoBanner(it) }
            uiState.errorMessage?.let { ErrorBanner(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.selectedCategory == StoreCategory.SIM_CARDS,
                    onClick = { viewModel.selectCategory(StoreCategory.SIM_CARDS) },
                    label = { Text("SIM Cards") }
                )
                FilterChip(
                    selected = uiState.selectedCategory == StoreCategory.AGENT_MATERIALS,
                    onClick = { viewModel.selectCategory(StoreCategory.AGENT_MATERIALS) },
                    label = { Text("Agent Materials") }
                )
            }

            LazyColumn(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    StoreItemCard(
                        item = item,
                        quantity = viewModel.quantityFor(item.id),
                        onQuantityChange = { viewModel.setQuantity(item.id, it) },
                        onBuy = { viewModel.openCheckout(item) }
                    )
                }
            }
        }
    }

    uiState.checkoutItem?.let { item ->
        AlertDialog(
            onDismissRequest = viewModel::dismissCheckout,
            title = { Text("Place Order") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${item.itemName} × ${viewModel.quantityFor(item.id)}")
                    Text(
                        "Total: GHS ${"%.2f".format(item.price * viewModel.quantityFor(item.id))}",
                        fontWeight = FontWeight.Bold
                    )
                    AgentTextField(
                        value = uiState.checkoutPhone,
                        onValueChange = viewModel::onCheckoutPhoneChange,
                        label = "Phone Number"
                    )
                    AgentTextField(
                        value = uiState.checkoutLocation,
                        onValueChange = viewModel::onCheckoutLocationChange,
                        label = "Location"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::placeOrder,
                    enabled = !uiState.isSubmitting
                ) {
                    Text(if (uiState.isSubmitting) "Submitting…" else "Buy")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissCheckout) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun StoreItemCard(
    item: StoreItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onBuy: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(item.itemName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(item.itemDescription, style = MaterialTheme.typography.bodySmall)
            Text("GHS ${"%.2f".format(item.price)}", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.TextButton(onClick = { onQuantityChange(quantity - 1) }) {
                        Text("−")
                    }
                    Text("Qty: $quantity")
                    androidx.compose.material3.TextButton(onClick = { onQuantityChange(quantity + 1) }) {
                        Text("+")
                    }
                }
                androidx.compose.material3.Button(onClick = onBuy) { Text("Buy") }
            }
        }
    }
}
