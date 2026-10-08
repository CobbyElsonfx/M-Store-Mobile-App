package com.momokotle.agent.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.models.AppNotification
import com.momokotle.agent.models.NotificationType
import com.momokotle.agent.ui.components.AppCard
import com.momokotle.agent.ui.components.AppEmptyState
import com.momokotle.agent.ui.components.AppFilterChip
import com.momokotle.agent.ui.components.AppScaffold
import com.momokotle.agent.ui.components.AppStatusBadge
import com.momokotle.agent.ui.components.AppStatusTone
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.AppFormStyles
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    var unreadOnly by rememberSaveable { mutableStateOf(false) }

    val unreadCount = notifications.count { !it.isRead }
    val visible = if (unreadOnly) notifications.filter { !it.isRead } else notifications

    AppScaffold(title = "Notifications", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal)
                .padding(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Inbox",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppFormStyles.onSurface
                        )
                        Text(
                            text = if (unreadCount == 0) {
                                "You're all caught up"
                            } else {
                                "$unreadCount unread"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (unreadCount == 0) AppColors.Success else AppColors.Warning
                        )
                    }
                    if (unreadCount > 0 && profile?.userId != null) {
                        TextButton(onClick = { viewModel.markAllAsRead(profile!!.userId) }) {
                            Text(
                                "Mark all read",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = AppFormStyles.onSurface
                            )
                        }
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing),
                verticalArrangement = Arrangement.spacedBy(AppDimens.ChipSpacing)
            ) {
                AppFilterChip(
                    label = "All",
                    selected = !unreadOnly,
                    onClick = { unreadOnly = false }
                )
                AppFilterChip(
                    label = "Unread",
                    selected = unreadOnly,
                    onClick = { unreadOnly = true }
                )
            }

            when {
                notifications.isEmpty() -> {
                    AppEmptyState(
                        title = "No notifications yet",
                        message = "Orders, commissions, referrals, and account alerts appear here.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                visible.isEmpty() -> {
                    AppEmptyState(
                        title = "No unread notifications",
                        message = "Switch to All to see earlier messages.",
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
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(visible, key = { it.notificationId }) { notification ->
                            NotificationItem(
                                notification = notification,
                                onClick = { viewModel.markAsRead(notification.notificationId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val tone = notification.type.statusTone()
    AppCard(onClick = if (notification.isRead) null else onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(8.dp)
                        .background(AppColors.Info, CircleShape)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title.ifBlank { notification.type.displayLabel() },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        color = AppFormStyles.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    AppStatusBadge(
                        text = notification.type.displayLabel(),
                        tone = tone
                    )
                }
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.label
                )
                Text(
                    text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                        .format(Date(notification.createdAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppFormStyles.label
                )
                if (!notification.isRead) {
                    Text(
                        text = "Tap to mark as read",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.Info
                    )
                }
            }
        }
    }
}

private fun NotificationType.displayLabel(): String = when (this) {
    NotificationType.REFERRAL_JOINED -> "Referral"
    NotificationType.COMMISSION_EARNED -> "Commission"
    NotificationType.SUBSCRIPTION_EXPIRED -> "Subscription"
    NotificationType.WITHDRAWAL_APPROVED -> "Withdrawal"
    NotificationType.ORDER_COMPLETED -> "Order"
    NotificationType.GENERAL -> "General"
}

private fun NotificationType.statusTone(): AppStatusTone = when (this) {
    NotificationType.COMMISSION_EARNED,
    NotificationType.WITHDRAWAL_APPROVED,
    NotificationType.ORDER_COMPLETED -> AppStatusTone.Success
    NotificationType.SUBSCRIPTION_EXPIRED -> AppStatusTone.Warning
    NotificationType.REFERRAL_JOINED -> AppStatusTone.Info
    NotificationType.GENERAL -> AppStatusTone.Neutral
}
