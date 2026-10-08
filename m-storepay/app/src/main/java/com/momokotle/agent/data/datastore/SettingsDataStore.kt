package com.momokotle.agent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.momokotle.agent.models.AppSettings
import com.momokotle.agent.models.AutomationSpeed
import com.momokotle.agent.models.DuplicateTransactionWarningWindow
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitDefaults
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.utils.PercentFormatter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "momo_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val AUTOMATION_SPEED = stringPreferencesKey("automation_speed")
        val DEFAULT_SEND_CONFIRM = booleanPreferencesKey("default_send_confirm")
        val CASH_IN_CALCULATION_MODE = stringPreferencesKey("cash_in_calculation_mode")
        val CASH_OUT_CALCULATION_MODE = stringPreferencesKey("cash_out_calculation_mode")
        val DEFAULT_CASH_IN_PERCENT = stringPreferencesKey("default_cash_in_percent")
        val DEFAULT_CASH_OUT_PERCENT = stringPreferencesKey("default_cash_out_percent")
        val DEFAULT_CASH_IN_FIXED = stringPreferencesKey("default_cash_in_fixed")
        val DEFAULT_CASH_OUT_FIXED = stringPreferencesKey("default_cash_out_fixed")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val TRANSACTION_VERIFICATION = booleanPreferencesKey("transaction_verification")
        val VIBRATE_ON_TOUCH = booleanPreferencesKey("vibrate_on_touch")
        val DUPLICATE_TX_WARNING_WINDOW = stringPreferencesKey("duplicate_tx_warning_window")
        val SIM_MTN_MOMO = stringPreferencesKey("sim_mtn_momo")
        val SIM_TELECEL_CASH = stringPreferencesKey("sim_telecel_cash")
        val SIM_AIRTELTIGO_CASH = stringPreferencesKey("sim_airteltigo_cash")
        val SIM_MTN_AIRTIME = stringPreferencesKey("sim_mtn_airtime")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            automationSpeed = AutomationSpeed.fromStored(prefs[Keys.AUTOMATION_SPEED]),
            defaultSendConfirm = prefs[Keys.DEFAULT_SEND_CONFIRM] ?: true,
            cashInCalculationMode = ProfitCalculationMode.fromStored(prefs[Keys.CASH_IN_CALCULATION_MODE]),
            cashOutCalculationMode = ProfitCalculationMode.fromStored(prefs[Keys.CASH_OUT_CALCULATION_MODE]),
            defaultCashInPercentage = parseStoredPercent(prefs[Keys.DEFAULT_CASH_IN_PERCENT]),
            defaultCashOutPercentage = parseStoredPercent(prefs[Keys.DEFAULT_CASH_OUT_PERCENT]),
            defaultCashInFixedCommission = parseStoredFixed(prefs[Keys.DEFAULT_CASH_IN_FIXED]),
            defaultCashOutFixedCommission = parseStoredFixed(prefs[Keys.DEFAULT_CASH_OUT_FIXED]),
            darkTheme = prefs[Keys.DARK_THEME],
            transactionVerificationEnabled = prefs[Keys.TRANSACTION_VERIFICATION] ?: false,
            vibrateOnTouch = prefs[Keys.VIBRATE_ON_TOUCH] ?: false,
            duplicateTransactionWarningWindow = DuplicateTransactionWarningWindow.fromStored(
                prefs[Keys.DUPLICATE_TX_WARNING_WINDOW]
            ),
            simMtnMomo = parseSimSlot(prefs[Keys.SIM_MTN_MOMO]),
            simTelecelCash = parseSimSlot(prefs[Keys.SIM_TELECEL_CASH]),
            simAirtelTigoCash = parseSimSlot(prefs[Keys.SIM_AIRTELTIGO_CASH]),
            simMtnAirtimeTransfer = parseSimSlot(prefs[Keys.SIM_MTN_AIRTIME])
        )
    }

    suspend fun updateAutomationSpeed(speed: AutomationSpeed) {
        context.dataStore.edit { it[Keys.AUTOMATION_SPEED] = speed.name }
    }

    suspend fun updateCashInCalculationMode(mode: ProfitCalculationMode) {
        context.dataStore.edit { it[Keys.CASH_IN_CALCULATION_MODE] = mode.name }
    }

    suspend fun updateCashOutCalculationMode(mode: ProfitCalculationMode) {
        context.dataStore.edit { it[Keys.CASH_OUT_CALCULATION_MODE] = mode.name }
    }

    suspend fun updateDefaultCashInPercent(displayPercent: String) {
        context.dataStore.edit { it[Keys.DEFAULT_CASH_IN_PERCENT] = displayPercent }
    }

    suspend fun updateDefaultCashOutPercent(displayPercent: String) {
        context.dataStore.edit { it[Keys.DEFAULT_CASH_OUT_PERCENT] = displayPercent }
    }

    suspend fun updateDefaultCashInFixed(displayAmount: String) {
        context.dataStore.edit { it[Keys.DEFAULT_CASH_IN_FIXED] = displayAmount }
    }

    suspend fun updateDefaultCashOutFixed(displayAmount: String) {
        context.dataStore.edit { it[Keys.DEFAULT_CASH_OUT_FIXED] = displayAmount }
    }

    suspend fun updateDarkTheme(enabled: Boolean?) {
        context.dataStore.edit {
            if (enabled == null) {
                it.remove(Keys.DARK_THEME)
            } else {
                it[Keys.DARK_THEME] = enabled
            }
        }
    }

    suspend fun updateTransactionVerification(enabled: Boolean) {
        context.dataStore.edit { it[Keys.TRANSACTION_VERIFICATION] = enabled }
    }

    suspend fun updateVibrateOnTouch(enabled: Boolean) {
        context.dataStore.edit { it[Keys.VIBRATE_ON_TOUCH] = enabled }
    }

    suspend fun updateDuplicateTransactionWarningWindow(window: DuplicateTransactionWarningWindow) {
        context.dataStore.edit { it[Keys.DUPLICATE_TX_WARNING_WINDOW] = window.name }
    }

    suspend fun updateSimSlot(key: Preferences.Key<String>, slot: SimSlot) {
        context.dataStore.edit { it[key] = slot.name }
    }

    suspend fun updateSimMtnMomo(slot: SimSlot) = updateSimSlot(Keys.SIM_MTN_MOMO, slot)
    suspend fun updateSimTelecelCash(slot: SimSlot) = updateSimSlot(Keys.SIM_TELECEL_CASH, slot)
    suspend fun updateSimAirtelTigoCash(slot: SimSlot) = updateSimSlot(Keys.SIM_AIRTELTIGO_CASH, slot)
    suspend fun updateSimMtnAirtimeTransfer(slot: SimSlot) = updateSimSlot(Keys.SIM_MTN_AIRTIME, slot)

    private fun parseSimSlot(value: String?): SimSlot =
        value?.let { runCatching { SimSlot.valueOf(it) }.getOrNull() } ?: SimSlot.NONE

    private fun parseStoredPercent(value: String?): Double {
        if (value == null) return ProfitDefaults.INITIAL_DECIMAL_RATE
        return PercentFormatter.parsePercentInput(value) ?: ProfitDefaults.INITIAL_DECIMAL_RATE
    }

    private fun parseStoredFixed(value: String?): Double {
        if (value == null) return ProfitDefaults.INITIAL_FIXED_COMMISSION
        return value.trim().toDoubleOrNull()?.takeIf { it >= 0 } ?: ProfitDefaults.INITIAL_FIXED_COMMISSION
    }
}
