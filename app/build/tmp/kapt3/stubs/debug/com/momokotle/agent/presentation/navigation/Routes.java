package com.momokotle.agent.presentation.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001b\n\u0002\u0010\t\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u0004J\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u0004J\u000e\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/momokotle/agent/presentation/navigation/Routes;", "", "()V", "ABOUT", "", "ACCOUNT_PROFILE", "AGENT_FORM", "AGENT_REGISTRATION", "APP_SETTINGS", "CASH_IN_SETTINGS", "CASH_OUT_SETTINGS", "CONTACTS", "CONTACT_SUPPORT", "CUSTOM_FLOWS", "CUSTOM_MENU_EXECUTION", "HISTORY", "HOME", "LOGIN", "MY_ORDERS", "MY_SUBMISSIONS", "ORDER_DETAIL", "PROFIT_CALCULATION", "REFERRAL", "REGISTER", "SETTINGS", "STORE", "SUBMISSION_DETAIL", "TRANSACTIONS", "agentForm", "formType", "customMenuExecution", "flowId", "", "orderDetail", "orderId", "submissionDetail", "submissionId", "app_debug"})
public final class Routes {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOGIN = "login";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String REGISTER = "register";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HOME = "home";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STORE = "store";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AGENT_REGISTRATION = "agent_registration";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AGENT_FORM = "agent_form/{formType}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HISTORY = "transactions";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TRANSACTIONS = "transactions";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CUSTOM_FLOWS = "custom_flows";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CUSTOM_MENU_EXECUTION = "custom_menu_execution/{flowId}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SETTINGS = "settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACCOUNT_PROFILE = "account_profile";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String APP_SETTINGS = "app_settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CONTACT_SUPPORT = "contact_support";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ABOUT = "about";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CONTACTS = "contacts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROFIT_CALCULATION = "profit_calculation";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CASH_IN_SETTINGS = "cash_in_settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CASH_OUT_SETTINGS = "cash_out_settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String REFERRAL = "referral";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MY_SUBMISSIONS = "my_submissions";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MY_ORDERS = "my_orders";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ORDER_DETAIL = "order_detail/{orderId}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SUBMISSION_DETAIL = "submission_detail/{submissionId}";
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.presentation.navigation.Routes INSTANCE = null;
    
    private Routes() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String agentForm(@org.jetbrains.annotations.NotNull()
    java.lang.String formType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String orderDetail(@org.jetbrains.annotations.NotNull()
    java.lang.String orderId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String submissionDetail(@org.jetbrains.annotations.NotNull()
    java.lang.String submissionId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String customMenuExecution(long flowId) {
        return null;
    }
}