package com.momokotle.agent.presentation.settings

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.domain.usecase.LogoutUseCase
import com.momokotle.agent.models.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsHomeViewModel @Inject constructor(
    userAccountDataStore: UserAccountDataStore,
    private val logoutUseCase: LogoutUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val account: StateFlow<UserAccount?> = userAccountDataStore.account
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _isLoggingOut.update { true }
            logoutUseCase()
            _isLoggingOut.update { false }
        }
    }

    fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(com.momokotle.agent.R.string.share_app_text))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(
            Intent.createChooser(intent, context.getString(com.momokotle.agent.R.string.share_app_chooser)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    userAccountDataStore: UserAccountDataStore
) : ViewModel() {

    val account: StateFlow<UserAccount?> = userAccountDataStore.account
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
