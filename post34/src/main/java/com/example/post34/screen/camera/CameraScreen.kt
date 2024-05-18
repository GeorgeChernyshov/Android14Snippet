package com.example.post34.screen.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraDevice.StateCallback
import android.hardware.camera2.CameraExtensionSession
import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.ExtensionSessionConfiguration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post34.R
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.uistate.CameraScreenUiState
import com.example.post34.viewmodel.CameraViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(),
    onNextClicked: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Camera.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                RealtimeLatencyBlock(
                    state = state.value,
                    onExtensionStatusChange = { extensionStatus ->
                        viewModel.setExtensionStatus(extensionStatus)
                    },
                    onStillCaptureLatencyChanged = { latency ->
                        viewModel.setCaptureLatency(latency?.captureLatency.toString())
                        viewModel.setProcessingLatency(latency?.processingLatency.toString())
                    }
                )

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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun RealtimeLatencyBlock(
    modifier: Modifier = Modifier,
    state: CameraScreenUiState,
    onExtensionStatusChange: (CameraScreenUiState.ExtensionStatus) -> Unit = {},
    onStillCaptureLatencyChanged: (StillCaptureLatency?) -> Unit = {}
) {
    Column(modifier) {
        val context = LocalContext.current
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        var stillCaptureLatency: StillCaptureLatency? = null

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                7892
            )

            return
        }

        cameraManager.openCamera(cameraId, object : StateCallback() {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun onOpened(camera: CameraDevice) {
                val callback = object : CameraExtensionSession.StateCallback() {
                    override fun onConfigured(session: CameraExtensionSession) {
                        stillCaptureLatency = session.realtimeStillCaptureLatency
                        onStillCaptureLatencyChanged.invoke(stillCaptureLatency)
                    }

                    override fun onConfigureFailed(p0: CameraExtensionSession) {
                        TODO("Not yet implemented")
                    }
                }

                val extension = cameraManager.getCameraExtensionCharacteristics(cameraId)
                    .supportedExtensions
                    .firstOrNull()

                if (extension == null) {
                    onExtensionStatusChange.invoke(
                        CameraScreenUiState.ExtensionStatus.NOT_AVAILABLE
                    )

                    return
                } else onExtensionStatusChange.invoke(
                    CameraScreenUiState.ExtensionStatus.AVAILABLE
                )

                camera.createExtensionSession(
                    createExtensionSessionConfiguration(extension, callback)
                )
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                camera.close()
            }
        }, null)

        when (state.extensionStatus) {
            CameraScreenUiState.ExtensionStatus.NOT_AVAILABLE -> {
                Text(text = stringResource(id = R.string.camera_no_extensions))
            }

            CameraScreenUiState.ExtensionStatus.AVAILABLE -> {
                Text(text = stringResource(
                    id = R.string.camera_capture_latency,
                    state.captureLatency ?: "Unknown"
                ))

                Text(text = stringResource(
                    id = R.string.camera_processing_latency,
                    state.processingLatency ?: "Unknown"
                ))
            }

            else -> {}
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun createExtensionSessionConfiguration(
    extension: Int,
    callback: CameraExtensionSession.StateCallback
) : ExtensionSessionConfiguration {
    return ExtensionSessionConfiguration(
        extension,
        emptyList(),
        { return@ExtensionSessionConfiguration },
        callback
    )
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true)
@Composable
fun RealtimeLatencyBlockPreview() {
    RealtimeLatencyBlock(state = CameraScreenUiState.DEFAULT)
}