package com.momokotle.agent.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.momokotle.agent.ui.components.AppLoadingState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.presentation.auth.SessionViewModel
import com.momokotle.agent.presentation.navigation.AuthNavHost
import com.momokotle.agent.presentation.navigation.MomoBottomBar
import com.momokotle.agent.presentation.navigation.MomoNavHost
import com.momokotle.agent.ui.theme.MomoKotleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsDataStore.settings.collectAsStateWithLifecycle(
                initialValue = com.momokotle.agent.models.AppSettings()
            )
            val darkTheme = settings.darkTheme ?: androidx.compose.foundation.isSystemInDarkTheme()
            val sessionViewModel: SessionViewModel = hiltViewModel()
            val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

            MomoKotleTheme(darkTheme = darkTheme) {
                when {
                    sessionState.isLoading -> {
                        AppLoadingState(modifier = Modifier.fillMaxSize())
                    }
                    sessionState.isAuthenticated -> {
                        val navController = rememberNavController()
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
