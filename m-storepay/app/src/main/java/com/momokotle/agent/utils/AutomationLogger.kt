package com.momokotle.agent.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class AutomationLogEntry(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Singleton
class AutomationLogger @Inject constructor() {
    private val _logs = MutableStateFlow<List<AutomationLogEntry>>(emptyList())
    val logs: StateFlow<List<AutomationLogEntry>> = _logs.asStateFlow()

    fun log(message: String) {
        runCatching { Log.d(TAG, message) }
        _logs.value = (_logs.value + AutomationLogEntry(message)).takeLast(MAX_LOGS)
    }

    fun clear() {
        _logs.value = emptyList()
    }

    companion object {
        private const val TAG = "MomoAutomation"
        private const val MAX_LOGS = 100

        fun formatTimestamp(timestamp: Long): String {
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
    }
}
