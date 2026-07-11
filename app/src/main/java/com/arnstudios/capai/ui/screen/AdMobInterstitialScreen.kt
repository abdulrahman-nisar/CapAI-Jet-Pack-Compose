package com.arnstudios.capshotai.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arnstudios.capshotai.ui.loading.BlobLoadingScreen
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun AdMobInterstitialScreen(
    itemIndex: Int,
    onAdDismissed: (Int) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler {
        // Do nothing to prevent skipping the ad screen
    }

    val loadingState = remember { mutableStateOf(true) }

    if(loadingState.value){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BlobLoadingScreen(label = "Loading Ad...")
        }
    }

    LaunchedEffect(Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3092242649828627/5345025700",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdDismissed(itemIndex)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdDismissed(itemIndex)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            onAdDismissed(itemIndex)
                        }
                    }
                    activity?.let {
                        loadingState.value = false
                        interstitialAd.show(it)
                    } ?: onAdDismissed(itemIndex)
                }
            }
        )
    }

}
