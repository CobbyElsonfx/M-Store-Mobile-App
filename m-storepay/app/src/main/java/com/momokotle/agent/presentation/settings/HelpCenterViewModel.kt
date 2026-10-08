package com.momokotle.agent.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.usecase.ObserveBusinessSettingsUseCase
import com.momokotle.agent.utils.WhatsAppSupportHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HelpCenterUiState(
    val loading: Boolean = true,
    val supportWhatsAppNumber: String = "",
    val errorMessage: String? = null
) {
    val hasWhatsAppSupport: Boolean get() = supportWhatsAppNumber.isNotBlank()
}

@HiltViewModel
class HelpCenterViewModel @Inject constructor(
    observeBusinessSettingsUseCase: ObserveBusinessSettingsUseCase
) : ViewModel() {

    val uiState: StateFlow<HelpCenterUiState> = observeBusinessSettingsUseCase()
        .map { settings ->
            HelpCenterUiState(
                loading = false,
                supportWhatsAppNumber = settings.supportWhatsAppNumber
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HelpCenterUiState()
        )

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    fun openWhatsAppChat(context: Context): Boolean {
        val digits = WhatsAppSupportHelper.normalizeDigits(uiState.value.supportWhatsAppNumber)
        if (digits.isNullOrBlank()) {
            _actionError.value = "WhatsApp support is not configured yet. Please try again later."
            return false
        }
        val opened = WhatsAppSupportHelper.openChat(context, digits)
        if (!opened) {
            _actionError.value = "Could not open WhatsApp. Install WhatsApp or try again from a browser."
        } else {
            _actionError.value = null
        }
        return opened
    }

    fun clearActionError() {
        _actionError.update { null }
    }
}
