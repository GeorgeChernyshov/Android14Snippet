package com.example.post34.navigation

import androidx.annotation.StringRes
import com.example.post34.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Core : Screen("core", R.string.label_core)
}