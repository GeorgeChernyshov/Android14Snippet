package com.example.post34.screen.screenshots

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.R
import com.example.post34.viewmodel.ScreenshotDetectionViewModel

@Composable
fun ScreenshotDetectionScreen(viewModel: ScreenshotDetectionViewModel = viewModel()) {
    val state = viewModel.state.collectAsState()
    
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
                
                if (state.value.isTaken)
                    Text(text = stringResource(id = R.string.screenshot_noticed))
            }
        }
    )
}