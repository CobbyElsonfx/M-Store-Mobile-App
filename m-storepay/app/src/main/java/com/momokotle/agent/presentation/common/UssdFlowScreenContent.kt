package com.momokotle.agent.presentation.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.momokotle.agent.domain.ussd.UssdAccessResult
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.ui.components.AgentActionButton
import com.momokotle.agent.ui.components.ErrorBanner
import com.momokotle.agent.ui.components.InfoBanner

@Composable
fun ColumnScope.UssdFlowScreenContent(
    uiState: UssdFlowUiState,
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    onOpenAccessibility: () -> Unit,
    formContent: @Composable ColumnScope.() -> Unit,
    onSubscribe: (() -> Unit)? = null
) {
    formContent()

    uiState.errorMessage?.let { ErrorBanner(it) }
    uiState.infoMessage?.let { InfoBanner(it) }

    Spacer(modifier = Modifier.height(8.dp))

    AgentActionButton(
        text = if (uiState.isLoading) "Starting…" else primaryLabel,
        onClick = onPrimaryClick,
        enabled = !uiState.isLoading && !uiState.automationStatus.isActive
    )

    if (onSubscribe != null && uiState.subscriptionAccess != UssdAccessResult.ALLOWED) {
        AgentActionButton(
            text = uiState.subscriptionAccess.actionLabel,
            onClick = onSubscribe
        )
    }

    if (!uiState.accessibilityEnabled) {
        AgentActionButton(text = "Enable Accessibility", onClick = onOpenAccessibility)
    }
}
