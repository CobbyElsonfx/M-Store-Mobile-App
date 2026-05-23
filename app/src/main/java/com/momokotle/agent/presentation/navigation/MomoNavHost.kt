package com.momokotle.agent.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.momokotle.agent.presentation.registration.AgentFormScreen
import com.momokotle.agent.presentation.registration.AgentRegistrationScreen
import com.momokotle.agent.presentation.store.StoreScreen
import com.momokotle.agent.presentation.contacts.ContactsScreen
import com.momokotle.agent.presentation.profit.CashInSettingsScreen
import com.momokotle.agent.presentation.profit.CashOutSettingsScreen
import com.momokotle.agent.presentation.profit.ProfitCalculationScreen
import com.momokotle.agent.presentation.custommenu.CustomFlowListScreen
import com.momokotle.agent.presentation.dashboard.DashboardScreen
import com.momokotle.agent.presentation.referral.ReferralScreen
import com.momokotle.agent.presentation.settings.AboutScreen
import com.momokotle.agent.presentation.settings.AccountProfileScreen
import com.momokotle.agent.presentation.settings.AppSettingsScreen
import com.momokotle.agent.presentation.settings.ContactSupportScreen
import com.momokotle.agent.presentation.settings.SettingsHomeScreen
import com.momokotle.agent.presentation.submissions.MySubmissionsScreen
import com.momokotle.agent.presentation.transactions.TransactionsScreen

@Composable
fun MomoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) { entry ->
            DashboardScreen(
                onCustomMenu = { navController.navigate(Routes.CUSTOM_FLOWS) },
                savedStateHandle = entry.savedStateHandle
            )
        }
        composable(Routes.STORE) {
            StoreScreen()
        }
        composable(Routes.AGENT_REGISTRATION) {
            AgentRegistrationScreen(
                onFormClick = { type ->
                    navController.navigate(Routes.agentForm(type.name))
                }
            )
        }
        composable(
            route = Routes.AGENT_FORM,
            arguments = listOf(navArgument("formType") { type = NavType.StringType })
        ) {
            AgentFormScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.HISTORY) {
            TransactionsScreen(isRootTab = true)
        }
        composable(Routes.CUSTOM_FLOWS) {
            CustomFlowListScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsHomeScreen(
                onAccountProfile = { navController.navigate(Routes.ACCOUNT_PROFILE) },
                onAppSettings = { navController.navigate(Routes.APP_SETTINGS) },
                onCalculation = { navController.navigate(Routes.PROFIT_CALCULATION) },
                onCashInSettings = { navController.navigate(Routes.CASH_IN_SETTINGS) },
                onCashOutSettings = { navController.navigate(Routes.CASH_OUT_SETTINGS) },
                onSavedContacts = { navController.navigate(Routes.CONTACTS) },
                onContactSupport = { navController.navigate(Routes.CONTACT_SUPPORT) },
                onAbout = { navController.navigate(Routes.ABOUT) },
                onReferral = { navController.navigate(Routes.REFERRAL) },
                onMySubmissions = { navController.navigate(Routes.MY_SUBMISSIONS) }
            )
        }
        composable(Routes.REFERRAL) {
            ReferralScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MY_SUBMISSIONS) {
            MySubmissionsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CONTACTS) {
            ContactsScreen(
                onBack = { navController.popBackStack() },
                onUseContact = { contact ->
                    navController.getBackStackEntry(Routes.HOME).savedStateHandle.apply {
                        set("prefill_phone", contact.phoneNumber)
                        set("prefill_network", contact.network.name)
                        set("prefill_ported", contact.isPorted)
                    }
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Routes.ACCOUNT_PROFILE) {
            AccountProfileScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.APP_SETTINGS) {
            AppSettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CONTACT_SUPPORT) {
            ContactSupportScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PROFIT_CALCULATION) {
            ProfitCalculationScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CASH_IN_SETTINGS) {
            CashInSettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CASH_OUT_SETTINGS) {
            CashOutSettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
