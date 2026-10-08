package com.momokotle.agent.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momokotle.agent.ui.components.AppAuthCard
import com.momokotle.agent.ui.components.AppButton
import com.momokotle.agent.ui.components.AppButtonStyle
import com.momokotle.agent.ui.components.AppErrorBanner
import com.momokotle.agent.ui.components.AppLoadingState
import com.momokotle.agent.ui.components.AppLogoHeader
import com.momokotle.agent.ui.components.AppTextField
import com.momokotle.agent.ui.theme.AppDimens

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess()
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogoHeader(
                title = "Welcome back",
                subtitle = "Sign in to your agent account"
            )

            AppAuthCard {
                AppTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email"
                )
                AppTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Password",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                uiState.errorMessage?.let { AppErrorBanner(message = it) }
                if (uiState.isLoading) {
                    AppLoadingState(modifier = Modifier.height(52.dp))
                } else {
                    AppButton(
                        text = "Login",
                        onClick = viewModel::login,
                        enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                        style = AppButtonStyle.Accent
                    )
                }
            }

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "Create an account",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            viewModel.consumeRegisterSuccess()
            onRegisterSuccess()
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SectionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogoHeader(
                title = "Create account",
                subtitle = "Register as a MoMo agent"
            )

            AppAuthCard {
                AppTextField(
                    value = uiState.fullName,
                    onValueChange = viewModel::onFullNameChange,
                    label = "Full Name"
                )
                AppTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email"
                )
                AppTextField(
                    value = uiState.phoneNumber,
                    onValueChange = viewModel::onPhoneChange,
                    label = "Phone Number",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                AppTextField(
                    value = uiState.location,
                    onValueChange = viewModel::onLocationChange,
                    label = "Location"
                )
                AppTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Password",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                AppTextField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Confirm Password",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                uiState.errorMessage?.let { AppErrorBanner(message = it) }
                if (uiState.isLoading) {
                    AppLoadingState(modifier = Modifier.height(52.dp))
                } else {
                    AppButton(
                        text = "Register",
                        onClick = viewModel::register,
                        style = AppButtonStyle.Accent
                    )
                }
            }

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Already have an account? Sign in",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
