package com.momokotle.agent.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.usecase.MarkAllNotificationsReadUseCase
import com.momokotle.agent.domain.usecase.MarkNotificationReadUseCase
import com.momokotle.agent.domain.usecase.ObserveNotificationsUseCase
import com.momokotle.agent.models.AppNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase
) : ViewModel() {

    val notifications: StateFlow<List<AppNotification>> = authRepository.observeAuthState()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) flowOf(emptyList())
            else observeNotificationsUseCase(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val profile = authRepository.observeAuthState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun markAsRead(notificationId: String) {
        viewModelScope.launch { markNotificationReadUseCase(notificationId) }
    }

    fun markAllAsRead(userId: String) {
        viewModelScope.launch { markAllNotificationsReadUseCase(userId) }
    }
}
