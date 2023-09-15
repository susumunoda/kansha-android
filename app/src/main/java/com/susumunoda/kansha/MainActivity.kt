package com.susumunoda.kansha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.susumunoda.android.auth.AuthController
import com.susumunoda.android.auth.SessionListenerHandler
import com.susumunoda.kansha.ui.theme.KanshaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authController: AuthController

    // Top-level injection is necessary as this handler is not a direct dependency of any other type
    @Inject
    lateinit var sessionListenerHandler: SessionListenerHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            KanshaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KanshaApp(authController)
                }
            }
        }
    }
}
