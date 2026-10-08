package com.momokotle.agent.presentation.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.components.AgentTextField
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.utils.InternetMessages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onMyOrders: () -> Unit = {},
    viewModel: StoreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.refreshConnectivity() }

    AppScaffold(
        title = "Store",
        actions = {
            TextButton(onClick = onMyOrders) {
                Text("My Orders", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 8.dp)
        ) {
            if (!uiState.isOnline) {
                AppErrorBanner(
                    message = "Offline — showing cached products. ${InternetMessages.STORE_REQUIRES_INTERNET}",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            uiState.message?.let { AppInfoBanner(it) }
            uiState.errorMessage?.let { AppErrorBanner(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing)) {
                AppFilterChip(
                    label = "SIM Cards",
                    selected = uiState.selectedCategory == StoreCategory.SIM_CARDS,
                    onClick = { viewModel.selectCategory(StoreCategory.SIM_CARDS) }
                )
                AppFilterChip(
                    label = "Agent Materials",
                    selected = uiState.selectedCategory == StoreCategory.AGENT_MATERIALS,
                    onClick = { viewModel.selectCategory(StoreCategory.AGENT_MATERIALS) }
                )
            }

            when {
                uiState.isLoadingProducts && uiState.products.isEmpty() -> {
                    AppLoadingState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp)
                    )
                }
                uiState.products.isEmpty() -> {
                    AppEmptyState(
                        title = "No products in this category",
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.products, key = { it.productId }) { product ->
                            StoreProductCard(
                                product = product,
                                quantity = viewModel.quantityFor(product.productId),
                                isOnline = uiState.isOnline,
                                onQuantityChange = { viewModel.setQuantity(product.productId, it) },
                                onBuy = { viewModel.openCheckout(product) }
                            )
                        }
                    }
                }
            }
        }
    }

    uiState.checkoutProduct?.let { product ->
        AlertDialog(
            onDismissRequest = viewModel::dismissCheckout,
            title = { Text("Place Order") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${product.productName} × ${viewModel.quantityFor(product.productId)}")
                    Text(
                        "Total: GHS ${"%.2f".format(product.price * viewModel.quantityFor(product.productId))}",
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
private fun StoreProductCard(
    product: StoreProduct,
    quantity: Int,
    isOnline: Boolean,
    onQuantityChange: (Int) -> Unit,
    onBuy: () -> Unit
) {
    val canBuy = product.canPurchase && isOnline
    val maxQty = product.stockQuantity.coerceAtLeast(1).coerceAtMost(99)

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ProductImage(
                imageUrl = product.productImageUrl,
                contentDescription = product.productName
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    product.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    product.productDescription,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "GHS ${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                val stockLabel = when {
                    product.isOutOfStock -> "Out of stock"
                    else -> "In stock: ${product.stockQuantity}"
                }
                AppStatusBadge(
                    text = stockLabel,
                    tone = if (product.isOutOfStock) AppStatusTone.Error else AppStatusTone.Success
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onQuantityChange(quantity - 1) },
                            enabled = canBuy && quantity > 1
                        ) { Text("−") }
                        Text("Qty: $quantity")
                        TextButton(
                            onClick = { onQuantityChange(quantity + 1) },
                            enabled = canBuy && quantity < maxQty
                        ) { Text("+") }
                    }
                    AppButton(
                        text = when {
                            !isOnline -> "Offline"
                            product.isOutOfStock -> "Out of Stock"
                            !product.isAvailable -> "Unavailable"
                            else -> "Buy"
                        },
                        onClick = onBuy,
                        enabled = canBuy,
                        style = AppButtonStyle.Accent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductImage(imageUrl: String?, contentDescription: String) {
    val shape = RoundedCornerShape(12.dp)
    if (!imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(88.dp)
                .clip(shape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No image",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
