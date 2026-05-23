package com.momokotle.agent.presentation.navigation

object Routes {
    const val HOME = "home"
    const val STORE = "store"
    const val AGENT_REGISTRATION = "agent_registration"
    const val AGENT_FORM = "agent_form/{formType}"
    const val HISTORY = "transactions"
    const val TRANSACTIONS = "transactions"
    const val CUSTOM_FLOWS = "custom_flows"
    const val SETTINGS = "settings"
    const val ACCOUNT_PROFILE = "account_profile"
    const val APP_SETTINGS = "app_settings"
    const val CONTACT_SUPPORT = "contact_support"
    const val ABOUT = "about"
    const val CONTACTS = "contacts"
    const val PROFIT_CALCULATION = "profit_calculation"
    const val CASH_IN_SETTINGS = "cash_in_settings"
    const val CASH_OUT_SETTINGS = "cash_out_settings"

    fun agentForm(formType: String) = "agent_form/$formType"
}

object BottomNavItem {
    const val HOME = Routes.HOME
    const val STORE = Routes.STORE
    const val REGISTER = Routes.AGENT_REGISTRATION
    const val HISTORY = Routes.HISTORY
    const val SETTINGS = Routes.SETTINGS
}
