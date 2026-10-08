package com.momokotle.agent.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.momokotle.agent.models.OrderStatus
import com.momokotle.agent.models.StoreOrder
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppDetailRow
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.theme.AppDimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    viewModel: MyOrdersViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    AppScaffold(title = "My Orders", onBack = onBack) { padding ->
        if (orders.isEmpty()) {
            AppEmptyState(
                title = "No store orders yet",
                message = "Orders you place in the Store appear here",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimens.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
            ) {
                items(orders, key = { it.orderId }) { order ->
                    OrderListCard(
                        order = order,
                        onClick = { onOrderClick(order.orderId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        val current = order
        if (current == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Order not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OrderStatusBadge(status = current.status)
                DetailRow("Order ID", current.orderId)
                DetailRow("Product", current.itemName)
                DetailRow("Quantity", current.quantity.toString())
                DetailRow("Total", "GHS ${"%.2f".format(current.totalAmount)}")
                DetailRow("Phone", current.phoneNumber)
                DetailRow("Location", current.location)
                DetailRow("Status", current.status.firestoreValue)
                DetailRow("Placed", formatOrderDate(current.createdAt))
                DetailRow("Updated", formatOrderDate(current.updatedAt))
            }
        }
    }
}

@Composable
private fun OrderListCard(order: StoreOrder, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RowWithBadge(order)
            Text(
                text = "${order.itemName} × ${order.quantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "GHS ${"%.2f".format(order.totalAmount)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = order.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = formatOrderDate(order.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun RowWithBadge(order: StoreOrder) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Order",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        OrderStatusBadge(status = order.status)
    }
}

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val tone = when (status) {
        OrderStatus.PENDING -> AppStatusTone.Warning
        OrderStatus.PAID, OrderStatus.PROCESSING -> AppStatusTone.Info
        OrderStatus.DELIVERED, OrderStatus.COMPLETED -> AppStatusTone.Success
        OrderStatus.CANCELLED -> AppStatusTone.Error
    }
    AppStatusBadge(text = status.firestoreValue, tone = tone)
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatOrderDate(timestamp: Long): String {
    if (timestamp <= 0L) return "—"
    return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
}
