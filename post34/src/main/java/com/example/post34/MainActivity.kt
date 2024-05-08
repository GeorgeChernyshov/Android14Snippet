package com.example.post34

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.post34.navigation.Screen
import com.example.post34.screen.core.CoreScreen
import com.example.post34.screen.grammar.GIScreen
import com.example.post34.screen.screenshots.ScreenshotDetectionActivity
import com.example.post34.screen.screenshots.ScreenshotDetectionScreen
import com.example.post34.screen.ux.UserExperienceScreen
import com.example.post34.theme.Android14SnippetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { App() }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Android14SnippetTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Core.route
        ) {
            composable(Screen.Core.route) {
                CoreScreen { navController.navigate(Screen.UserExperience.route) }
            }

            composable(Screen.UserExperience.route) {
                UserExperienceScreen {
                    navController.navigate(Screen.Grammar.route)
                }
            }

            composable(Screen.Grammar.route) {
                GIScreen {
                    context.startActivity(Intent(context, ScreenshotDetectionActivity::class.java))
                }
            }
        }
    }
}