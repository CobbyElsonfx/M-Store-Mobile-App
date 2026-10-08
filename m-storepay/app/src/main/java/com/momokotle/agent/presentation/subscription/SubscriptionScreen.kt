package com.momokotle.agent.presentation.subscription

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.models.SubscriptionStatus
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSectionHeader
import com.momokotle.agent.ui.components.AppSuccessBanner
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun SubscriptionScreen(
    onBack: () -> Unit,
    onPurchaseSuccess: () -> Unit = {},
    onManualPay: (String) -> Unit = {},
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onScreenVisible()
        }
    }
    val checkoutLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> viewModel.onCheckoutCompleted(
                result.data?.getStringExtra(PaystackCheckoutActivity.EXTRA_REFERENCE)
            )
            PaystackCheckoutActivity.RESULT_FAILED -> viewModel.onCheckoutFailed(
                result.data?.getStringExtra(PaystackCheckoutActivity.EXTRA_ERROR)
            )
            else -> viewModel.onCheckoutCancelled()
        }
    }

    LaunchedEffect(uiState.accessCodeToLaunch) {
        val accessCode = uiState.accessCodeToLaunch ?: return@LaunchedEffect
        val publicKey = uiState.checkout?.publicKey.orEmpty()
        viewModel.consumeLaunch()
        if (publicKey.isBlank()) {
            viewModel.onCheckoutFailed("Paystack public key was not provided by the server.")
            return@LaunchedEffect
        }
        checkoutLauncher.launch(
            PaystackCheckoutActivity.intent(context, publicKey, accessCode)
        )
    }

    LaunchedEffect(uiState.purchaseSuccess) {
        if (uiState.purchaseSuccess) {
            viewModel.consumePurchaseSuccess()
            onPurchaseSuccess()
        }
    }

    AppScaffold(title = "Subscription", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            val profile = uiState.profile
            val access = profile?.accessLevel ?: AccessLevel.LIMITED
            val paidActive = profile?.subscriptionStatus == SubscriptionStatus.ACTIVE
            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Current status", style = MaterialTheme.typography.titleMedium, color = AppFormStyles.onSurface)
                Text(
                    text = when {
                        paidActive -> "Subscription Active"
                        access == AccessLevel.FULL -> "Full Access"
                        access == AccessLevel.SUBSCRIPTION_REQUIRED -> "Subscription Required"
                        else -> "Limited Access"
                    },
                    fontWeight = FontWeight.Bold,
                    color = when {
                        paidActive || access == AccessLevel.FULL -> AppColors.Success
                        access == AccessLevel.SUBSCRIPTION_REQUIRED -> AppColors.Warning
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                profile?.trialEndDate?.let {
                    Text("Trial ends: ${SimpleDateFormat.getDateInstance().format(Date(it))}")
                }
                profile?.subscriptionExpiryDate?.let {
                    Text("Subscription expires: ${SimpleDateFormat.getDateInstance().format(Date(it))}")
                }
                }
            }

            if (!uiState.settings.subscriptionEnabled) {
                AppErrorBanner(message = "Subscriptions are currently disabled")
                return@Column
            }

            AppSectionHeader(title = "Choose a plan")
            uiState.plans.forEach { plan ->
                PlanCard(
                    plan = plan,
                    selected = uiState.selectedPlanId == plan.planId,
                    currency = uiState.settings.currency,
                    onSelect = { viewModel.selectPlan(plan.planId) }
                )
            }

            uiState.selectedPlanId?.let { selectedId ->
                val selected = uiState.plans.find { it.planId == selectedId }
                if (selected != null) {
                    AppCard {
                        Text("Review", style = MaterialTheme.typography.titleMedium)
                        Text(selected.planName, fontWeight = FontWeight.SemiBold)
                        Text("${uiState.settings.currency} ${selected.price} · ${selected.durationDays} days")
                        Text(
                            "Pay with Paystack (Mobile Money or card) or pay manually using the bank or MoMo details set by admin.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            uiState.paymentStatus?.let { payment ->
                AppCard {
                    Text("Payment status", style = MaterialTheme.typography.titleMedium)
                    Text(paymentStatusLabel(payment.status), fontWeight = FontWeight.SemiBold)
                    payment.planName?.takeIf { it.isNotBlank() }?.let { Text(it) }
                    Text("Reference: ${payment.reference}", style = MaterialTheme.typography.bodySmall)
                    if (payment.amount > 0) {
                        Text("${payment.currency} ${payment.amount}")
                    }
                }
            }

            uiState.infoMessage?.let { message ->
                if (uiState.purchaseSuccess || uiState.paymentStatus?.isConfirmed == true) {
                    AppSuccessBanner(message)
                } else {
                    AppInfoBanner(message)
                }
            }
            uiState.errorMessage?.let { AppErrorBanner(message = it) }

            if (uiState.isLoading) {
                Text("Initializing payment…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                AppLoadingState()
            } else if (uiState.isCheckingStatus) {
                Text("Verifying payment…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                AppLoadingState()
            } else {
                val canPay = uiState.selectedPlanId != null && !uiState.isCheckingStatus && !paidActive
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(
                        text = "Paystack",
                        onClick = viewModel::payNow,
                        modifier = Modifier.weight(1f),
                        enabled = canPay,
                        style = AppButtonStyle.Accent
                    )
                    AppButton(
                        text = "Pay manually",
                        onClick = { uiState.selectedPlanId?.let(onManualPay) },
                        modifier = Modifier.weight(1f),
                        enabled = canPay,
                        style = AppButtonStyle.Outlined
                    )
                }
                if (uiState.checkout != null) {
                    AppButton(
                        text = if (uiState.isCheckingStatus) "Checking…" else "Check status",
                        onClick = viewModel::checkStatus,
                        enabled = !uiState.isCheckingStatus,
                        style = AppButtonStyle.Outlined
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    selected: Boolean,
    currency: String,
    onSelect: () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(plan.planName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("$currency ${plan.price} · ${plan.durationDays} days")
            AppButton(
                text = if (selected) "Selected" else "Select",
                onClick = onSelect,
                style = if (selected) AppButtonStyle.Accent else AppButtonStyle.Outlined
            )
        }
    }
}

private fun paymentStatusLabel(status: String): String = when (status.lowercase()) {
    "pending", "ongoing" -> "Processing"
    "confirmed" -> "Paid"
    "abandoned" -> "Cancelled"
    "reversed" -> "Reversed"
    "failed" -> "Failed"
    else -> status
}
