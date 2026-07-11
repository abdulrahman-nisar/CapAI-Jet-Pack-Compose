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
import com.arnstudios.capshotai.domain.model.Length
import com.arnstudios.capshotai.ui.loading.BlobLoadingScreen
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

@Composable
fun AdMobRewardedScreen(
    selectedLength: Length,
    onAdDismissed: (Length) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val loadingState = remember { mutableStateOf(true) }


    BackHandler {
        // Do nothing to prevent skipping the ad screen
    }

    if(loadingState.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BlobLoadingScreen(label = "Loading Reward Ad...")
        }
    }

    LaunchedEffect(Unit) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            "ca-app-pub-3092242649828627/8818769222",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdDismissed(selectedLength)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdDismissed(selectedLength)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            onAdDismissed(selectedLength)
                        }
                    }
                    activity?.let {
                        loadingState.value = false
                        rewardedAd.show(it) { rewardItem ->
                            // User earned the reward
                        }
                    } ?: onAdDismissed(selectedLength)
                }
            }
        )
    }


}
