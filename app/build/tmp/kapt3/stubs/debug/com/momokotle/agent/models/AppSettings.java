package com.momokotle.agent.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b*\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\u0002\u0010\u0014J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0010H\u00c6\u0003J\t\u0010+\u001a\u00020\u0010H\u00c6\u0003J\t\u0010,\u001a\u00020\u0010H\u00c6\u0003J\t\u0010-\u001a\u00020\u0010H\u00c6\u0003J\t\u0010.\u001a\u00020\u0005H\u00c6\u0003J\t\u0010/\u001a\u00020\u0007H\u00c6\u0003J\t\u00100\u001a\u00020\u0007H\u00c6\u0003J\t\u00101\u001a\u00020\nH\u00c6\u0003J\t\u00102\u001a\u00020\nH\u00c6\u0003J\u0010\u00103\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\t\u00104\u001a\u00020\u0005H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\u0092\u0001\u00106\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u0010H\u00c6\u0001\u00a2\u0006\u0002\u00107J\u0013\u00108\u001a\u00020\u00052\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020;H\u00d6\u0001J\u000e\u0010<\u001a\u00020\u00102\u0006\u0010=\u001a\u00020>J\t\u0010?\u001a\u00020@H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0015\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0012\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010#R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010#R\u0011\u0010\u0011\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010#R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010!R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010!\u00a8\u0006A"}, d2 = {"Lcom/momokotle/agent/models/AppSettings;", "", "automationSpeed", "Lcom/momokotle/agent/models/AutomationSpeed;", "defaultSendConfirm", "", "cashInCalculationMode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "cashOutCalculationMode", "defaultCashInPercentage", "", "defaultCashOutPercentage", "darkTheme", "transactionVerificationEnabled", "vibrateOnTouch", "simMtnMomo", "Lcom/momokotle/agent/models/SimSlot;", "simTelecelCash", "simAirtelTigoCash", "simMtnAirtimeTransfer", "(Lcom/momokotle/agent/models/AutomationSpeed;ZLcom/momokotle/agent/models/ProfitCalculationMode;Lcom/momokotle/agent/models/ProfitCalculationMode;DDLjava/lang/Boolean;ZZLcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;)V", "getAutomationSpeed", "()Lcom/momokotle/agent/models/AutomationSpeed;", "getCashInCalculationMode", "()Lcom/momokotle/agent/models/ProfitCalculationMode;", "getCashOutCalculationMode", "getDarkTheme", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getDefaultCashInPercentage", "()D", "getDefaultCashOutPercentage", "getDefaultSendConfirm", "()Z", "getSimAirtelTigoCash", "()Lcom/momokotle/agent/models/SimSlot;", "getSimMtnAirtimeTransfer", "getSimMtnMomo", "getSimTelecelCash", "getTransactionVerificationEnabled", "getVibrateOnTouch", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Lcom/momokotle/agent/models/AutomationSpeed;ZLcom/momokotle/agent/models/ProfitCalculationMode;Lcom/momokotle/agent/models/ProfitCalculationMode;DDLjava/lang/Boolean;ZZLcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;Lcom/momokotle/agent/models/SimSlot;)Lcom/momokotle/agent/models/AppSettings;", "equals", "other", "hashCode", "", "simSlotFor", "purpose", "Lcom/momokotle/agent/models/SimPurpose;", "toString", "", "app_debug"})
public final class AppSettings {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.AutomationSpeed automationSpeed = null;
    private final boolean defaultSendConfirm = false;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitCalculationMode cashInCalculationMode = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.ProfitCalculationMode cashOutCalculationMode = null;
    private final double defaultCashInPercentage = 0.0;
    private final double defaultCashOutPercentage = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean darkTheme = null;
    private final boolean transactionVerificationEnabled = false;
    private final boolean vibrateOnTouch = false;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.SimSlot simMtnMomo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.SimSlot simTelecelCash = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.SimSlot simAirtelTigoCash = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.models.SimSlot simMtnAirtimeTransfer = null;
    
    public AppSettings(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.AutomationSpeed automationSpeed, boolean defaultSendConfirm, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashInCalculationMode, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashOutCalculationMode, double defaultCashInPercentage, double defaultCashOutPercentage, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean darkTheme, boolean transactionVerificationEnabled, boolean vibrateOnTouch, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simMtnMomo, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simTelecelCash, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simAirtelTigoCash, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simMtnAirtimeTransfer) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.AutomationSpeed getAutomationSpeed() {
        return null;
    }
    
    public final boolean getDefaultSendConfirm() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode getCashInCalculationMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode getCashOutCalculationMode() {
        return null;
    }
    
    public final double getDefaultCashInPercentage() {
        return 0.0;
    }
    
    public final double getDefaultCashOutPercentage() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean getDarkTheme() {
        return null;
    }
    
    public final boolean getTransactionVerificationEnabled() {
        return false;
    }
    
    public final boolean getVibrateOnTouch() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot getSimMtnMomo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot getSimTelecelCash() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot getSimAirtelTigoCash() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot getSimMtnAirtimeTransfer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot simSlotFor(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimPurpose purpose) {
        return null;
    }
    
    public AppSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.AutomationSpeed component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.SimSlot component13() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.ProfitCalculationMode component4() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.AppSettings copy(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.AutomationSpeed automationSpeed, boolean defaultSendConfirm, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashInCalculationMode, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.ProfitCalculationMode cashOutCalculationMode, double defaultCashInPercentage, double defaultCashOutPercentage, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean darkTheme, boolean transactionVerificationEnabled, boolean vibrateOnTouch, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simMtnMomo, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simTelecelCash, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simAirtelTigoCash, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.SimSlot simMtnAirtimeTransfer) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}