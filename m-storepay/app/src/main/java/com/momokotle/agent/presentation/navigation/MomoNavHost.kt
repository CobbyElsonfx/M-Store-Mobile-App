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
import com.momokotle.agent.presentation.custommenu.CustomMenuExecutionScreen
import com.momokotle.agent.presentation.dashboard.DashboardScreen
import com.momokotle.agent.presentation.referral.ReferralScreen
import com.momokotle.agent.presentation.subscription.ManualPaymentScreen
import com.momokotle.agent.presentation.subscription.SubscriptionScreen
import com.momokotle.agent.presentation.wallet.WalletScreen
import com.momokotle.agent.presentation.notifications.NotificationsScreen
import com.momokotle.agent.presentation.settings.AboutScreen
import com.momokotle.agent.presentation.settings.AccountProfileScreen
import com.momokotle.agent.presentation.settings.AppSettingsScreen
import com.momokotle.agent.presentation.settings.ContactSupportScreen
import com.momokotle.agent.presentation.settings.SettingsHomeScreen
import com.momokotle.agent.presentation.orders.MyOrdersScreen
import com.momokotle.agent.presentation.orders.OrderDetailScreen
import com.momokotle.agent.presentation.submissions.MySubmissionsScreen
import com.momokotle.agent.presentation.submissions.SubmissionDetailScreen
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
                onRunCustomMenu = { flowId ->
                    navController.navigate(Routes.customMenuExecution(flowId))
                },
                onSubscribe = { navController.navigate(Routes.SUBSCRIPTION) },
                savedStateHandle = entry.savedStateHandle
            )
        }
        composable(Routes.STORE) {
            StoreScreen(
                onMyOrders = { navController.navigate(Routes.MY_ORDERS) },
                onSubscribe = { navController.navigate(Routes.SUBSCRIPTION) },
                onProductForm = { formType, productId ->
                    navController.navigate(Routes.agentForm(formType.name, productId))
                }
            )
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
            arguments = listOf(
                navArgument("formType") { type = NavType.StringType },
                navArgument("productId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            AgentFormScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.HISTORY) {
            TransactionsScreen(isRootTab = true)
        }
        composable(Routes.CUSTOM_FLOWS) {
            CustomFlowListScreen(
                onBack = { navController.popBackStack() },
                onRunFlow = { flowId ->
                    navController.navigate(Routes.customMenuExecution(flowId))
                }
            )
        }
        composable(
            route = Routes.CUSTOM_MENU_EXECUTION,
            arguments = listOf(navArgument("flowId") { type = NavType.LongType })
        ) { backStackEntry ->
            val flowId = backStackEntry.arguments?.getLong("flowId") ?: 0L
            CustomMenuExecutionScreen(
                flowId = flowId,
                onBack = { navController.popBackStack() },
                onSubscribe = { navController.navigate(Routes.SUBSCRIPTION) }
            )
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
                onWallet = { navController.navigate(Routes.WALLET) },
                onSubscription = { navController.navigate(Routes.SUBSCRIPTION) },
                onNotifications = { navController.navigate(Routes.NOTIFICATIONS) },
                onMySubmissions = { navController.navigate(Routes.MY_SUBMISSIONS) },
                onMyOrders = { navController.navigate(Routes.MY_ORDERS) }
            )
        }
        composable(Routes.REFERRAL) {
            ReferralScreen(
                onBack = { navController.popBackStack() },
                onNavigateToWallet = { navController.navigate(Routes.WALLET) }
            )
        }
        composable(Routes.WALLET) {
            WalletScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SUBSCRIPTION) {
            SubscriptionScreen(
                onBack = { navController.popBackStack() },
                onManualPay = { planId ->
                    navController.navigate(Routes.subscriptionManual(planId))
                }
            )
        }
        composable(
            route = Routes.SUBSCRIPTION_MANUAL,
            arguments = listOf(navArgument("planId") { type = NavType.StringType })
        ) {
            ManualPaymentScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MY_SUBMISSIONS) {
            MySubmissionsScreen(
                onBack = { navController.popBackStack() },
                onSubmissionClick = { id ->
                    navController.navigate(Routes.submissionDetail(id))
                }
            )
        }
        composable(
            route = Routes.SUBMISSION_DETAIL,
            arguments = listOf(navArgument("submissionId") { type = NavType.StringType })
        ) {
            val submissionId = it.arguments?.getString("submissionId").orEmpty()
            SubmissionDetailScreen(
                submissionId = submissionId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.MY_ORDERS) {
            MyOrdersScreen(
                onBack = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(Routes.orderDetail(orderId))
                }
            )
        }
        composable(
            route = Routes.ORDER_DETAIL,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) {
            val orderId = it.arguments?.getString("orderId").orEmpty()
            OrderDetailScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
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
