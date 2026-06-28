package com.arnstudios.capai.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.arnstudios.capai.ui.CapAiViewModel
import com.arnstudios.capai.ui.screen.AdMobInterstitialScreen
import com.arnstudios.capai.ui.screen.AdMobRewardedScreen
import com.arnstudios.capai.ui.screen.CaptionPreferencesScreen
import com.arnstudios.capai.ui.screen.DetailsScreen
import com.arnstudios.capai.ui.screen.HomeDetailsScreen
import com.arnstudios.capai.ui.screen.HomeScreen
import com.arnstudios.capai.ui.screen.OnboardingScreen
import com.arnstudios.capai.ui.screen.SelectImageScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavigationRoot(viewModel: CapAiViewModel) {

    val startRoute = if (viewModel.isOnboardingCompleted) Route.HomeScreen else Route.OnboardingScreen
    val backStack = rememberNavBackStack(startRoute)

    NavDisplay(
        backStack,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally { it } + fadeIn(),
                initialContentExit = slideOutHorizontally { -it } + fadeOut()
            )
        },
        popTransitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally { -it } + fadeIn(),
                initialContentExit = slideOutHorizontally { it } + fadeOut()
            )
        },
        entryProvider = entryProvider{
            entry<Route.OnboardingScreen> {
                OnboardingScreen(
                    onFinish = {
                        viewModel.setOnboardingCompleted()
                        backStack.removeLast()
                        backStack.add(Route.HomeScreen)
                    }
                )
            }
            entry<Route.HomeScreen> {
                HomeScreen(
                    viewModel = viewModel,
                    onNewBtnClick = {
                        backStack.add(Route.SelectImageScreen)
                    },
                    onHistoryItemClick = { index ->
                        backStack.add(Route.AdMobInterstitialScreen(index))
                    }
                )
            }
            entry<Route.AdMobInterstitialScreen> { route ->
                AdMobInterstitialScreen(
                    itemIndex = route.itemIndex,
                    onAdDismissed = { index ->
                        backStack.removeLast()
                        backStack.add(Route.HomeDetailsScreen(index))
                    }
                )
            }
            entry<Route.HomeDetailsScreen> { route ->
                val historyList = viewModel.historyList.collectAsState().value
                if (route.itemIndex in historyList.indices) {
                    HomeDetailsScreen(
                        capAi = historyList[route.itemIndex],
                        onBackArrowClick = {
                            backStack.removeLast()
                        }
                    )
                }
            }
            entry<Route.SelectImageScreen> {
                 SelectImageScreen(
                     viewModel = viewModel,
                     onBackArrowClick = {
                            backStack.removeLast()
                     },
                     onSucessfulImagePick = {
                            backStack.add(Route.CaptionPreferencesScreen)
                     }
                 )
            }
            entry <Route.CaptionPreferencesScreen> {
                CaptionPreferencesScreen(
                    viewModel = viewModel,
                    onBackArrowClick = {
                        backStack.removeLast()
                    },
                    onGenerateCaptionClick = {
                        backStack.add(Route.AdMobRewardedScreen(it))
                    }
                )
            }
            entry <Route.AdMobRewardedScreen> { route ->
                AdMobRewardedScreen(
                    selectedLength = route.selectedLength,
                    onAdDismissed = { length ->
                        backStack.removeLast()
                        backStack.add(Route.DetailsScreen(length))
                    }
                )
            }
            entry <Route.DetailsScreen> { len->
                DetailsScreen(
                    viewModel = viewModel,
                    len.selectedLength,
                    onBackArrowClick = {
                        backStack.removeLast()
                        backStack.removeLast()
                        backStack.removeLast()
                    },
                    onIsSuccessFalse = {
                        backStack.removeLast()
                    }
                )
            }
        }
    )
}
