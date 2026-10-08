package com.momokotle.agent.repository

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.momokotle.agent.accessibility.MomoAccessibilityService
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.utils.AccessibilityHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AccessibilityRepository {

    private val _accessibilityStatus = MutableStateFlow(false)
    override val accessibilityStatus: StateFlow<Boolean> = _accessibilityStatus.asStateFlow()

    private val settingsObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            refresh()
        }
    }

    init {
        refresh()
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES),
            false,
            settingsObserver
        )
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_ENABLED),
            false,
            settingsObserver
        )
    }

    override fun isAccessibilityEnabled(): Boolean = _accessibilityStatus.value

    override fun refresh() {
        _accessibilityStatus.value = AccessibilityHelper.isAccessibilityServiceEnabled(
            context,
            MomoAccessibilityService::class.java
        )
    }

    override fun openAccessibilitySettings() {
        AccessibilityHelper.openAccessibilitySettings(context)
    }
}
