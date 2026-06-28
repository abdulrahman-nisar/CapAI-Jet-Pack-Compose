package com.arnstudios.capai

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnstudios.capai.ui.CapAiViewModel
import com.arnstudios.capai.ui.navigation.NavigationRoot
import com.arnstudios.capai.ui.theme.CapAiTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {}
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapAiTheme {
                val viewModel: CapAiViewModel = viewModel()
                NavigationRoot(viewModel)
            }
        }
    }
}
