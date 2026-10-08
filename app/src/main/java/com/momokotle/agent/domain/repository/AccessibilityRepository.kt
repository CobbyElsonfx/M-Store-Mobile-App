package com.momokotle.agent.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AccessibilityRepository {
    val accessibilityStatus: StateFlow<Boolean>

    fun isAccessibilityEnabled(): Boolean

    fun refresh()

    fun openAccessibilitySettings()
}
