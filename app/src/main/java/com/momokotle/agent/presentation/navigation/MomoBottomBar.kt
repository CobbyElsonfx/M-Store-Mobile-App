package com.momokotle.agent.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class BottomNavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val destinations = listOf(
    BottomNavDestination(BottomNavItem.HOME, "Home", Icons.Default.Home),
    BottomNavDestination(BottomNavItem.STORE, "Store", Icons.Default.Store),
    BottomNavDestination(BottomNavItem.REGISTER, "Register", Icons.Default.AppRegistration),
    BottomNavDestination(BottomNavItem.HISTORY, "History", Icons.Default.History),
    BottomNavDestination(BottomNavItem.SETTINGS, "Settings", Icons.Default.Settings)
)

private val subRoutes = setOf(
    Routes.CUSTOM_FLOWS,
    Routes.CONTACTS,
    Routes.PROFIT_CALCULATION,
    Routes.CASH_IN_SETTINGS,
    Routes.CASH_OUT_SETTINGS,
    Routes.ACCOUNT_PROFILE,
    Routes.APP_SETTINGS,
    Routes.CONTACT_SUPPORT,
    Routes.ABOUT,
    "agent_form"
)

@Composable
fun MomoBottomBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    if (currentRoute != null && subRoutes.any { prefix -> currentRoute.startsWith(prefix) }) {
        return
    }

    NavigationBar {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(BottomNavItem.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}
