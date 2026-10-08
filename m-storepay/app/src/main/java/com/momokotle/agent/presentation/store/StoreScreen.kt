package com.momokotle.agent.presentation.store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.models.StoreProductForms
import com.momokotle.agent.presentation.common.SubscriptionAccessGate
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.components.AppSuccessBanner
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.utils.InternetMessages

@Composable
fun StoreScreen(
    onMyOrders: () -> Unit = {},
    onSubscribe: () -> Unit = {},
    onProductForm: (AgentFormType, String) -> Unit = { _, _ -> },
    viewModel: StoreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.refreshConnectivity() }

    AppScaffold(
        title = "Store",
        actions = {
            TextButton(
                onClick = onMyOrders,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = "My Orders",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { padding ->
        SubscriptionAccessGate(
            profile = uiState.profile,
            onSubscribe = onSubscribe,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            StoreContent(
                uiState = uiState,
                viewModel = viewModel,
                onProductForm = onProductForm,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StoreContent(
    uiState: StoreUiState,
    viewModel: StoreViewModel,
    onProductForm: (AgentFormType, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.ScreenHorizontal)
                .padding(top = 8.dp)
        ) {
            if (!uiState.isOnline) {
                AppErrorBanner(
                    message = "Offline — showing cached products. ${InternetMessages.STORE_REQUIRES_INTERNET}",
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            uiState.message?.let {
                AppSuccessBanner(
                    it,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
            uiState.errorMessage?.let {
                AppErrorBanner(
                    it,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Text(
                text = "One item per order. Open a product to fill its form and pay.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppFormStyles.label,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
                verticalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing)
            ) {
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
                            .weight(1f)
                    )
                }
                uiState.products.isEmpty() -> {
                    AppEmptyState(
                        title = "No products in this category",
                        message = "Check back later or choose a different category.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
                    ) {
                        items(uiState.products, key = { it.productId }) { product ->
                            StoreProductCard(
                                product = product,
                                isOnline = uiState.isOnline,
                                onOpen = { viewModel.onProductClick(product, onProductForm) }
                            )
                        }
                    }
                }
            }
        }

        uiState.checkoutProduct?.let { product ->
            PlaceOrderDialog(
                product = product,
                phone = uiState.checkoutPhone,
                location = uiState.checkoutLocation,
                isSubmitting = uiState.isSubmitting,
                onPhoneChange = viewModel::onCheckoutPhoneChange,
                onLocationChange = viewModel::onCheckoutLocationChange,
                onConfirm = viewModel::placeOrder,
                onDismiss = viewModel::dismissCheckout
            )
        }
    }
}

@Composable
private fun StoreProductCard(
    product: StoreProduct,
    isOnline: Boolean,
    onOpen: () -> Unit
) {
    val formType = StoreProductForms.formTypeFor(product)
    val disabled = !product.isAvailable
    val canOpen = product.isAvailable && isOnline
    AppCard(
        onClick = if (canOpen) onOpen else null
    ) {
        Column(
            modifier = Modifier.alpha(if (disabled) 0.5f else 1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                imageUrl = product.productImageUrl,
                contentDescription = product.productName
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppFormStyles.onSurface
                )
                val description = product.productDescription
                    .takeIf { it.isNotBlank() && !it.equals(product.productName, ignoreCase = true) }
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppFormStyles.label
                    )
                }
                Text(
                    text = "GHS ${"%.2f".format(product.price)} · 1 item",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppFormStyles.onSurface
                )
                Text(
                    text = "Pay GHS ${"%.2f".format(product.price)} for this item.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppFormStyles.label
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppStatusBadge(
                        text = when {
                            disabled -> "Currently unavailable"
                            formType != null -> formType.serviceTitle
                            else -> "Direct order"
                        },
                        tone = AppStatusTone.Neutral
                    )
                    if (!isOnline) {
                        AppStatusBadge(text = "Offline", tone = AppStatusTone.Neutral)
                    }
                }
            }
            if (canOpen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open ${product.productName}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (!disabled) {
            Text(
                text = when {
                    !isOnline -> "Connect to the internet to order"
                    formType != null -> "Fill form & pay"
                    else -> "Order 1 item"
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (canOpen) AppFormStyles.onSurface else AppFormStyles.label,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        }

        if (disabled) {
            AppErrorBanner(
                message = "This product is currently unavailable.",
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun PlaceOrderDialog(
    product: StoreProduct,
    phone: String,
    location: String,
    isSubmitting: Boolean,
    onPhoneChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        containerColor = AppFormStyles.card,
        titleContentColor = AppFormStyles.onSurface,
        textContentColor = AppFormStyles.onSurface,
        title = {
            Text(
                text = "Place order",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppFormStyles.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppFormStyles.onSurface
                )
                Text(
                    text = "Quantity: 1",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.label
                )
                Text(
                    text = "Total: GHS ${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppFormStyles.onSurface
                )
                AppTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = "Phone number",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                AppTextField(
                    value = location,
                    onValueChange = onLocationChange,
                    label = "Delivery location"
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isSubmitting,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppFormStyles.onSurface,
                    disabledContentColor = AppFormStyles.label
                )
            ) {
                Text(
                    text = if (isSubmitting) "Submitting…" else "Pay GHS ${"%.2f".format(product.price)}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
                colors = ButtonDefaults.textButtonColors(contentColor = AppFormStyles.label)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ProductImage(imageUrl: String?, contentDescription: String) {
    val shape = RoundedCornerShape(12.dp)
    val imageModifier = Modifier
        .size(80.dp)
        .clip(shape)
        .border(1.dp, MaterialTheme.colorScheme.outline, shape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    if (!imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = imageModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SIM",
                style = MaterialTheme.typography.labelMedium,
                color = AppFormStyles.label,
                textAlign = TextAlign.Center
            )
        }
    }
}
