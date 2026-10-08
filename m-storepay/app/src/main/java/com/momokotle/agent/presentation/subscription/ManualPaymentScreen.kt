package com.momokotle.agent.presentation.subscription

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.usecase.CheckSubscriptionPaymentStatusUseCase
import com.momokotle.agent.domain.usecase.InitializeManualSubscriptionPaymentUseCase
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase
import com.momokotle.agent.domain.usecase.ObserveSubscriptionPlansUseCase
import com.momokotle.agent.domain.usecase.RefreshUserProfileUseCase
import com.momokotle.agent.models.ManualPaymentCheckout
import com.momokotle.agent.models.ManualPaymentDetails
import com.momokotle.agent.models.SubscriptionPaymentStatus
import com.momokotle.agent.models.SubscriptionPlan
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppInfoBanner
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppSuccessBanner
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManualPaymentUiState(
    val plan: SubscriptionPlan? = null,
    val checkout: ManualPaymentCheckout? = null,
    val paymentStatus: SubscriptionPaymentStatus? = null,
    val isLoading: Boolean = false,
    val isCheckingStatus: Boolean = false,
    val errorMessage: String? = null,
    val copiedLabel: String? = null
)

@HiltViewModel
class ManualPaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observePlansUseCase: ObserveSubscriptionPlansUseCase,
    observeSettingsUseCase: ObserveBusinessSettingsUseCase,
    private val initializeManualPaymentUseCase: InitializeManualSubscriptionPaymentUseCase,
    private val checkPaymentStatusUseCase: CheckSubscriptionPaymentStatusUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val planId: String = savedStateHandle.get<String>("planId").orEmpty()
    private val _actionState = MutableStateFlow(ManualPaymentUiState())

    val uiState: StateFlow<ManualPaymentUiState> = combine(
        observePlansUseCase(),
        observeSettingsUseCase(),
        _actionState
    ) { plans, settings, action ->
        val plan = plans.find { it.planId == planId }
        val checkout = action.checkout ?: if (plan != null && action.errorMessage == null) {
            ManualPaymentCheckout(
                reference = "",
                amount = plan.price,
                currency = settings.currency,
                planId = plan.planId,
                planName = plan.planName,
                status = "Pending",
                instructions = settings.manualPayment
            )
        } else {
            action.checkout
        }
        action.copy(plan = plan, checkout = checkout)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ManualPaymentUiState())

    init {
        startManualPayment()
    }

    fun onScreenVisible() {
        viewModelScope.launch {
            refreshUserProfileUseCase()
            val reference = _actionState.value.checkout?.reference
                ?: _actionState.value.paymentStatus?.reference
            if (!reference.isNullOrBlank()) {
                checkStatus(showErrors = false)
            }
        }
    }

    fun startManualPayment() {
        if (planId.isBlank()) {
            _actionState.update { it.copy(errorMessage = "Select a subscription plan") }
            return
        }
        if (!networkMonitor.isOnline()) {
            _actionState.update { it.copy(errorMessage = InternetMessages.SUBSCRIPTION_REQUIRES_INTERNET) }
            return
        }
        viewModelScope.launch {
            _actionState.update { it.copy(isLoading = true, errorMessage = null) }
            initializeManualPaymentUseCase(planId)
                .onSuccess { checkout ->
                    _actionState.update {
                        it.copy(
                            isLoading = false,
                            checkout = checkout,
                            paymentStatus = SubscriptionPaymentStatus(
                                reference = checkout.reference,
                                status = checkout.status,
                                amount = checkout.amount,
                                currency = checkout.currency,
                                planName = checkout.planName,
                                provider = "MANUAL"
                            )
                        )
                    }
                }
                .onFailure { error ->
                    _actionState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Could not load payment details")
                    }
                }
        }
    }

    fun checkStatus(showErrors: Boolean = true) {
        val reference = _actionState.value.checkout?.reference
            ?: _actionState.value.paymentStatus?.reference
        if (reference.isNullOrBlank()) {
            if (showErrors) {
                _actionState.update { it.copy(errorMessage = "No payment to check yet") }
            }
            return
        }
        viewModelScope.launch {
            _actionState.update { it.copy(isCheckingStatus = true, errorMessage = null) }
            checkPaymentStatusUseCase(reference)
                .onSuccess { status ->
                    _actionState.update {
                        it.copy(isCheckingStatus = false, paymentStatus = status)
                    }
                    if (status.isConfirmed) {
                        refreshUserProfileUseCase()
                    }
                }
                .onFailure { error ->
                    _actionState.update {
                        it.copy(
                            isCheckingStatus = false,
                            errorMessage = if (showErrors) {
                                error.message ?: "Could not check payment"
                            } else {
                                it.errorMessage
                            }
                        )
                    }
                }
        }
    }

    fun copyToClipboard(context: Context, label: String, value: String) {
        if (value.isBlank()) return
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
        _actionState.update { it.copy(copiedLabel = label) }
    }

    fun consumeCopied() {
        _actionState.update { it.copy(copiedLabel = null) }
    }
}

@Composable
fun ManualPaymentScreen(
    onBack: () -> Unit,
    viewModel: ManualPaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onScreenVisible()
        }
    }

    AppScaffold(title = "Manual payment", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing)
        ) {
            val checkout = uiState.checkout
            val plan = uiState.plan
            val status = uiState.paymentStatus

            AppCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Pay this amount", style = MaterialTheme.typography.titleMedium, color = AppFormStyles.onSurface)
                    Text(
                        text = checkout?.let { "${it.currency} ${it.amount}" }
                            ?: plan?.let { "GHS ${it.price}" }
                            ?: "—",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppFormStyles.onSurface
                    )
                    Text(
                        checkout?.planName ?: plan?.planName ?: "Subscription",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (uiState.isLoading) {
                AppLoadingState()
            }

            checkout?.let { details ->
                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Payment details", style = MaterialTheme.typography.titleMedium, color = AppFormStyles.onSurface)
                        CopyableDetail(
                            label = "Reference",
                            value = details.reference,
                            onCopy = { viewModel.copyToClipboard(context, "Reference", details.reference) }
                        )
                        PaymentInstructionRows(
                            instructions = details.instructions,
                            onCopy = { label, value -> viewModel.copyToClipboard(context, label, value) }
                        )
                    }
                }
            }

            when {
                status?.isConfirmed == true && status.subscriptionActive -> {
                    AppSuccessBanner("Payment confirmed. Your subscription is active.")
                }
                status?.isConfirmed == true -> {
                    AppSuccessBanner("Payment confirmed. Activating your subscription…")
                }
                else -> {
                    AppInfoBanner(
                        "Send the exact amount using the details above. Include your reference in the transfer note. An admin will assign your plan after the payment is received."
                    )
                }
            }

            uiState.copiedLabel?.let { AppSuccessBanner("Copied $it") }
            uiState.errorMessage?.let { AppErrorBanner(message = it) }

            AppButton(
                text = if (uiState.isCheckingStatus) "Checking…" else "Check status",
                onClick = { viewModel.checkStatus() },
                enabled = !uiState.isCheckingStatus && !uiState.isLoading,
                style = AppButtonStyle.Outlined
            )
        }
    }
}

@Composable
private fun PaymentInstructionRows(
    instructions: ManualPaymentDetails,
    onCopy: (String, String) -> Unit
) {
    val rows = listOf(
        "Account name" to instructions.accountName,
        "Account number" to instructions.accountNumber,
        "Bank" to instructions.bankName,
        "MoMo network" to instructions.momoNetwork,
        "MoMo number" to instructions.momoNumber
    )
    rows.filter { it.second.isNotBlank() }.forEach { (label, value) ->
        CopyableDetail(label = label, value = value, onCopy = { onCopy(label, value) })
    }
    if (instructions.instructions.isNotBlank()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Instructions", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(instructions.instructions, style = MaterialTheme.typography.bodyLarge, color = AppFormStyles.onSurface)
        }
    }
    if (instructions.supportContact.isNotBlank()) {
        CopyableDetail(
            label = "Support",
            value = instructions.supportContact,
            onCopy = { onCopy("Support", instructions.supportContact) }
        )
    }
}

@Composable
private fun CopyableDetail(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    if (value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = AppFormStyles.onSurface)
        }
        TextButton(onClick = onCopy) {
            Text("Copy")
        }
    }
}
