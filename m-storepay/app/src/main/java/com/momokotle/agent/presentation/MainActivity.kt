package com.momokotle.agent.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.momokotle.agent.ui.components.AppLoadingState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.momokotle.agent.presentation.auth.SessionViewModel
import com.momokotle.agent.presentation.navigation.AuthNavHost
import com.momokotle.agent.presentation.navigation.MomoBottomBar
import com.momokotle.agent.presentation.navigation.MomoNavHost
import com.momokotle.agent.ui.theme.MomoKotleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            val sessionViewModel: SessionViewModel = hiltViewModel()
            val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

            MomoKotleTheme(darkTheme = false) {
                val rootModifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                when {
                    sessionState.isLoading -> {
                        AppLoadingState(modifier = rootModifier)
                    }
                    sessionState.isAuthenticated -> {
                        LifecycleResumeEffect(Unit) {
                            sessionViewModel.refreshSubscriptionOnForeground()
                            onPauseOrDispose { }
                        }
                        val navController = rememberNavController()
                        Scaffold(
                            modifier = rootModifier,
                            containerColor = MaterialTheme.colorScheme.background,
                            contentWindowInsets = WindowInsets(0, 0, 0, 0),
                            bottomBar = { MomoBottomBar(navController) }
                        ) { padding ->
                            MomoNavHost(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                            )
                        }
                    }
                    else -> {
                        val authNavController = rememberNavController()
                        AuthNavHost(
                            navController = authNavController,
                            onAuthenticated = { /* SessionViewModel observes auth state */ },
                            modifier = rootModifier
                        )
                    }
                }
            }
        }
    }
}
