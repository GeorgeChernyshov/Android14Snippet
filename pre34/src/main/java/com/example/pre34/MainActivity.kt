package com.example.pre34

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.pre34.components.AppBar
import com.example.pre34.navigation.Screen
import com.example.pre34.theme.Android14SnippetTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var isScreenshotTaken by mutableStateOf(false)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                3009
            )
        }

        setContent { App(isScreenshotTaken) }
    }

    @OptIn(FlowPreview::class)
    override fun onStart() {
        super.onStart()

        job = CoroutineScope(Dispatchers.Main).launch {
            createContentObserverFlow()
                .debounce(500)
                .collect { uri ->
                    val path = getFilePathFromContentResolver(this@MainActivity, uri)

                    path?.let { p ->
                        if (isScreenshotPath(p)) {
                            isScreenshotTaken = true
                        }
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()

        job?.cancel()
    }

    private fun createContentObserverFlow() = channelFlow {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                uri?.let { _ ->
                    trySend(uri)
                }
            }
        }

        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )

        awaitClose {
            contentResolver.unregisterContentObserver(contentObserver)
        }
    }

    private fun getFilePathFromContentResolver(context: Context, uri: Uri): String? {
        try {
            context.contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
                ),
                null,
                null,
                null
            )?.let { cursor ->
                cursor.moveToFirst()
                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val path = if (index >= 0) cursor.getString(index) else null
                cursor.close()

                return path
            }
        }
        catch (e: Exception) {
            Log.w(TAG, e.message ?: "")
        }

        return null
    }

    private fun isScreenshotPath(path: String?): Boolean {
        val lowercasePath = path?.lowercase()
        val screenshotDirectory = getPublicScreenshotDirectoryName()?.lowercase()
        return (screenshotDirectory != null &&
                lowercasePath?.contains(screenshotDirectory) == true) ||
                lowercasePath?.contains("screenshot") == true
    }

    private fun getPublicScreenshotDirectoryName() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS).name
    else null

    companion object {
        private const val TAG = "ScreenshotDetection"
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