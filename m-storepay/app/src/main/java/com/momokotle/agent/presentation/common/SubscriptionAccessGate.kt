package com.momokotle.agent.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.momokotle.agent.models.AccessLevel
import com.momokotle.agent.models.UserProfile
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.theme.AppFormStyles

@Composable
fun SubscriptionAccessGate(
    profile: UserProfile?,
    onSubscribe: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val access = profile?.accessLevel ?: AccessLevel.LIMITED
    if (access == AccessLevel.FULL) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (access) {
                AccessLevel.SUBSCRIPTION_REQUIRED -> "Your free trial or subscription has expired"
                AccessLevel.LIMITED -> "Subscribe to unlock premium features"
                else -> "Subscription required"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AppFormStyles.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Get full access to the store, referrals, and business tools.",
            style = MaterialTheme.typography.bodyLarge,
            color = AppFormStyles.label,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        AppButton(
            text = "View Subscription Plans",
            onClick = onSubscribe,
            style = AppButtonStyle.Accent
        )
    }
}
