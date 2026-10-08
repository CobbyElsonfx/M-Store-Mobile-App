package com.momokotle.agent.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation

/** @see AppButton */
@Composable
fun AgentActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = AppButtonStyle.Accent
    )
}

/** @see AppTextField */
@Composable
fun AgentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun ScreenHeader(title: String, subtitle: String? = null) {
    AppSectionHeader(title = title, subtitle = subtitle)
}

@Composable
fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    AppErrorBanner(message = message, modifier = modifier)
}

@Composable
fun InfoBanner(message: String, modifier: Modifier = Modifier) {
    AppInfoBanner(message = message, modifier = modifier)
}

@Composable
fun PageTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}
