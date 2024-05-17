package com.example.post34.screen.screensharing

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post34.MediaProjectionService
import com.example.post34.R
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.viewmodel.ScreenSharingViewModel

@Composable
fun ScreenSharingScreen(
    viewModel: ScreenSharingViewModel = viewModel(),
    onNextClicked: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current
    val mediaProjectionManager = context.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    val startMediaProjection = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = Intent(context, MediaProjectionService::class.java)
            intent.putExtra(
                MediaProjectionService.Extras.ACTION,
                MediaProjectionService.ActionValues.START
            )

            intent.putExtra(
                MediaProjectionService.Extras.DATA,
                result.data
            )

            intent.putExtra(
                MediaProjectionService.Extras.RESULT_CODE,
                result.resultCode
            )

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    79555
                )
            }

            context.startForegroundService(intent)
            viewModel.setSharing(true)
        }
    }

    val stopMediaProjection = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = Intent(context, MediaProjectionService::class.java)
            intent.putExtra(
                MediaProjectionService.Extras.ACTION,
                MediaProjectionService.ActionValues.STOP
            )

            context.startService(intent)
            viewModel.setSharing(false)
        }
    }

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.ScreenSharing.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(
                    id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                        R.string.screen_sharing_hint
                    else R.string.screen_sharing_not_available
                ))

                if (!state.value.isSharing) {
                    Button(onClick = {
                        startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
                    }) {
                        Text(text = stringResource(id = R.string.screen_sharing_start))
                    }
                } else {
                    Button(onClick = {
                        stopMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
                    }) {
                        Text(text = stringResource(id = R.string.screen_sharing_stop))
                    }
                }

                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { onNextClicked.invoke() }
                ) {
                    Text(text = stringResource(id = R.string.button_go_next))
                }
            }
        }
    )
}