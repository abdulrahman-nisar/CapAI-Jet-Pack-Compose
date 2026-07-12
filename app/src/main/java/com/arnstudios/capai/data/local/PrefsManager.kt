package com.arnstudios.capai.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("capai_prefs", Context.MODE_PRIVATE)

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean("onboarding_completed", false)
        set(value) = prefs.edit { putBoolean("onboarding_completed", value) }

    var onHistoryItemOpenedCount : Int
        get() = prefs.getInt("history_item_opened_count", 0)
        set(value) = prefs.edit { putInt("history_item_opened_count", value) }
}
