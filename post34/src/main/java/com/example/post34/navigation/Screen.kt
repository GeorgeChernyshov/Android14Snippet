package com.example.post34.navigation

import androidx.annotation.StringRes
import com.example.post34.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Core : Screen("core", R.string.label_core)
    object UserExperience : Screen("userExperience", R.string.label_user_experience)
    object Grammar : Screen("grammar", R.string.label_grammar)
    object ScreenshotDetection : Screen("screenshotDetection", R.string.label_screenshot_detection)
}