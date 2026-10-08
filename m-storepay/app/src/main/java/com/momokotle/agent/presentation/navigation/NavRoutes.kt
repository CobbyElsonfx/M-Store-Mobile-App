package com.momokotle.agent.presentation.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val STORE = "store"
    const val AGENT_REGISTRATION = "agent_registration"
    const val AGENT_FORM = "agent_form/{formType}?productId={productId}"
    const val HISTORY = "transactions"
    const val TRANSACTIONS = "transactions"
    const val CUSTOM_FLOWS = "custom_flows"
    const val CUSTOM_MENU_EXECUTION = "custom_menu_execution/{flowId}"
    const val SETTINGS = "settings"
    const val ACCOUNT_PROFILE = "account_profile"
    const val APP_SETTINGS = "app_settings"
    const val CONTACT_SUPPORT = "contact_support"
    const val ABOUT = "about"
    const val CONTACTS = "contacts"
    const val PROFIT_CALCULATION = "profit_calculation"
    const val CASH_IN_SETTINGS = "cash_in_settings"
    const val CASH_OUT_SETTINGS = "cash_out_settings"
    const val REFERRAL = "referral"
    const val WALLET = "wallet"
    const val SUBSCRIPTION = "subscription"
    const val SUBSCRIPTION_MANUAL = "subscription_manual/{planId}"
    const val NOTIFICATIONS = "notifications"
    const val MY_SUBMISSIONS = "my_submissions"
    const val MY_ORDERS = "my_orders"
    const val ORDER_DETAIL = "order_detail/{orderId}"
    const val SUBMISSION_DETAIL = "submission_detail/{submissionId}"

    fun agentForm(formType: String, productId: String? = null): String {
        val encodedProduct = productId?.takeIf { it.isNotBlank() }.orEmpty()
        return "agent_form/$formType?productId=$encodedProduct"
    }
    fun orderDetail(orderId: String) = "order_detail/$orderId"
    fun submissionDetail(submissionId: String) = "submission_detail/$submissionId"
    fun customMenuExecution(flowId: Long) = "custom_menu_execution/$flowId"
    fun subscriptionManual(planId: String) = "subscription_manual/$planId"
}

object BottomNavItem {
    const val HOME = Routes.HOME
    const val STORE = Routes.STORE
    const val REGISTER = Routes.AGENT_REGISTRATION
    const val HISTORY = Routes.HISTORY
    const val SETTINGS = Routes.SETTINGS
}
