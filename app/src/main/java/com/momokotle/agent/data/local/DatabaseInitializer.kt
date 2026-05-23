package com.momokotle.agent.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor() {
    fun seedDefaults() {
        // Custom profit rules are vendor-defined; no default seed data.
    }
}
