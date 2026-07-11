package com.arnstudios.capshotai.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.arnstudios.capshotai.domain.model.Length
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey{
    @Serializable
    data object OnboardingScreen : Route, NavKey
    @Serializable
    data object HomeScreen : Route, NavKey
    @Serializable
    data object SelectImageScreen : Route, NavKey
    @Serializable
    data object CaptionPreferencesScreen : Route, NavKey
    @Serializable
    data class DetailsScreen(val selectedLength: Length) : Route, NavKey
    @Serializable
    data class AdMobRewardedScreen(val selectedLength: Length) : Route, NavKey
    @Serializable
    data class AdMobInterstitialScreen(val itemIndex: Int) : Route, NavKey
    @Serializable
    data class HomeDetailsScreen(val itemIndex: Int) : Route, NavKey
}