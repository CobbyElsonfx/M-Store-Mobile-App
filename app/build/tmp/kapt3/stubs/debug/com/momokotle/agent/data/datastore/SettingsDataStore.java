package com.momokotle.agent.data.datastore;

import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.momokotle.agent.models.AppSettings;
import com.momokotle.agent.models.AutomationSpeed;
import com.momokotle.agent.models.ProfitCalculationMode;
import com.momokotle.agent.models.ProfitDefaults;
import com.momokotle.agent.models.SimSlot;
import com.momokotle.agent.utils.PercentFormatter;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001:\u00010B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0018\u0010\u001b\u001a\u00020\u00132\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010#\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010%J\u0016\u0010&\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010%J\u0016\u0010\'\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010%J$\u0010(\u001a\u00020\u00132\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\r0*2\u0006\u0010$\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010%J\u0016\u0010-\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001dH\u0086@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001dH\u0086@\u00a2\u0006\u0002\u0010.R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u00061"}, d2 = {"Lcom/momokotle/agent/data/datastore/SettingsDataStore;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "settings", "Lkotlinx/coroutines/flow/Flow;", "Lcom/momokotle/agent/models/AppSettings;", "getSettings", "()Lkotlinx/coroutines/flow/Flow;", "parseCalculationMode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "value", "", "parseSimSlot", "Lcom/momokotle/agent/models/SimSlot;", "parseStoredPercent", "", "updateAutomationSpeed", "", "speed", "Lcom/momokotle/agent/models/AutomationSpeed;", "(Lcom/momokotle/agent/models/AutomationSpeed;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCashInCalculationMode", "mode", "(Lcom/momokotle/agent/models/ProfitCalculationMode;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCashOutCalculationMode", "updateDarkTheme", "enabled", "", "(Ljava/lang/Boolean;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDefaultCashInPercent", "displayPercent", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDefaultCashOutPercent", "updateSimAirtelTigoCash", "slot", "(Lcom/momokotle/agent/models/SimSlot;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSimMtnAirtimeTransfer", "updateSimMtnMomo", "updateSimSlot", "key", "Landroidx/datastore/preferences/core/Preferences$Key;", "(Landroidx/datastore/preferences/core/Preferences$Key;Lcom/momokotle/agent/models/SimSlot;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSimTelecelCash", "updateTransactionVerification", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateVibrateOnTouch", "Keys", "app_debug"})
public final class SettingsDataStore {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.momokotle.agent.models.AppSettings> settings = null;
    
    @javax.inject.Inject()
    public SettingsDataStore(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.momokotle.agent.models.AppSettings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateAutomationSpeed(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.AutomationSpeed speed, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCashInCalculationMode(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCashOutCalculationMode(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateDefaultCashInPercent(@org.jetbrains.annotations.NotNull()
    java.lang.String displayPercent, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateDefaultCashOutPercent(@org.jetbrains.annotations.NotNull()
    java.lang.String displayPercent, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateDarkTheme(@org.jetbrains.annotations.Nullable()
    java.lang.Boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateTransactionVerification(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateVibrateOnTouch(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSimSlot(@org.jetbrains.annotations.NotNull()
    androidx.datastore.preferences.core.Preferences.Key<java.lang.String> key, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSimMtnMomo(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSimTelecelCash(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSimAirtelTigoCash(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSimMtnAirtimeTransfer(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot slot, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.momokotle.agent.models.SimSlot parseSimSlot(java.lang.String value) {
        return null;
    }
    
    private final double parseStoredPercent(java.lang.String value) {
        return 0.0;
    }
    
    private final com.momokotle.agent.models.ProfitCalculationMode parseCalculationMode(java.lang.String value) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0014\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0007R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0007R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0007R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0007R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0007R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0007R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0007R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0007R\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0007\u00a8\u0006!"}, d2 = {"Lcom/momokotle/agent/data/datastore/SettingsDataStore$Keys;", "", "()V", "AUTOMATION_SPEED", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "getAUTOMATION_SPEED", "()Landroidx/datastore/preferences/core/Preferences$Key;", "CASH_IN_CALCULATION_MODE", "getCASH_IN_CALCULATION_MODE", "CASH_OUT_CALCULATION_MODE", "getCASH_OUT_CALCULATION_MODE", "DARK_THEME", "", "getDARK_THEME", "DEFAULT_CASH_IN_PERCENT", "getDEFAULT_CASH_IN_PERCENT", "DEFAULT_CASH_OUT_PERCENT", "getDEFAULT_CASH_OUT_PERCENT", "DEFAULT_SEND_CONFIRM", "getDEFAULT_SEND_CONFIRM", "SIM_AIRTELTIGO_CASH", "getSIM_AIRTELTIGO_CASH", "SIM_MTN_AIRTIME", "getSIM_MTN_AIRTIME", "SIM_MTN_MOMO", "getSIM_MTN_MOMO", "SIM_TELECEL_CASH", "getSIM_TELECEL_CASH", "TRANSACTION_VERIFICATION", "getTRANSACTION_VERIFICATION", "VIBRATE_ON_TOUCH", "getVIBRATE_ON_TOUCH", "app_debug"})
    static final class Keys {
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> AUTOMATION_SPEED = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> DEFAULT_SEND_CONFIRM = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> CASH_IN_CALCULATION_MODE = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> CASH_OUT_CALCULATION_MODE = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> DEFAULT_CASH_IN_PERCENT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> DEFAULT_CASH_OUT_PERCENT = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> DARK_THEME = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> TRANSACTION_VERIFICATION = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> VIBRATE_ON_TOUCH = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> SIM_MTN_MOMO = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> SIM_TELECEL_CASH = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> SIM_AIRTELTIGO_CASH = null;
        @org.jetbrains.annotations.NotNull()
        private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> SIM_MTN_AIRTIME = null;
        @org.jetbrains.annotations.NotNull()
        public static final com.momokotle.agent.data.datastore.SettingsDataStore.Keys INSTANCE = null;
        
        private Keys() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getAUTOMATION_SPEED() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getDEFAULT_SEND_CONFIRM() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getCASH_IN_CALCULATION_MODE() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getCASH_OUT_CALCULATION_MODE() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getDEFAULT_CASH_IN_PERCENT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getDEFAULT_CASH_OUT_PERCENT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getDARK_THEME() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getTRANSACTION_VERIFICATION() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getVIBRATE_ON_TOUCH() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getSIM_MTN_MOMO() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getSIM_TELECEL_CASH() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getSIM_AIRTELTIGO_CASH() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> getSIM_MTN_AIRTIME() {
            return null;
        }
    }
}