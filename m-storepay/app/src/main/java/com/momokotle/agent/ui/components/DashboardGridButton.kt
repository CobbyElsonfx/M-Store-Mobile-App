package com.momokotle.agent.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun DashboardGridButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    highlighted: Boolean = false,
    showCustomBadge: Boolean = false,
    compact: Boolean = true,
    featured: Boolean = false,
    locked: Boolean = false
) {
    AppActionGridButton(
        label = label,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        icon = icon,
        highlighted = highlighted,
        showCustomBadge = showCustomBadge,
        compact = compact,
        featured = featured,
        locked = locked
    )
}

@Composable
fun DashboardGridSpacer(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Spacer(
        modifier = modifier.height(AppDimens.GridButtonHeight)
    )
}
