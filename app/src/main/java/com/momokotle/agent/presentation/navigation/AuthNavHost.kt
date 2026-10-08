package com.momokotle.agent.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.momokotle.agent.presentation.auth.LoginScreen
import com.momokotle.agent.presentation.auth.RegisterScreen

@Composable
fun AuthNavHost(
    navController: NavHostController,
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = onAuthenticated,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = onAuthenticated,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
