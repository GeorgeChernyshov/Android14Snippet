package com.example.post34.screen.screenshots

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post34.R
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.theme.Android14SnippetTheme
import com.example.post34.viewmodel.ScreenshotDetectionViewModel

class ScreenshotDetectionActivity : ComponentActivity() {

    var isScreenshotTaken by mutableStateOf(false)

    val screenCaptureCallback = ScreenCaptureCallback {
        isScreenshotTaken = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { App(isScreenshotTaken) }
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            registerScreenCaptureCallback(mainExecutor, screenCaptureCallback)
    }

    override fun onStop() {
        super.onStop()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            unregisterScreenCaptureCallback(screenCaptureCallback)
    }
}

@Composable
fun App(isScreenshotTaken: Boolean) {
    Android14SnippetTheme {
        Scaffold(
            topBar = { AppBar(name = stringResource(id = Screen.ScreenshotDetection.resourceId)) },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.screenshot_try))

                    if (isScreenshotTaken)
                        Text(text = stringResource(id = R.string.screenshot_noticed))
                }
            }
        )
    }
}