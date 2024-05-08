package com.example.pre34.navigation

import androidx.annotation.StringRes
import com.example.pre34.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object ScreenshotDetection : Screen("screenshotDetection", R.string.label_screenshot_detection)
}