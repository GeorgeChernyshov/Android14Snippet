package com.example.post34

import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class MediaProjectionService : Service() {

    private val mediaProjectionManager : MediaProjectionManager by lazy {
        getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private lateinit var path : String

    private var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var handler: Handler? = null
    private var virtualDisplay: VirtualDisplay? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        getExternalFilesDir(null)?.let {
            path = it.absolutePath.plus("/sharing/")
            val storeDirectory = File(path)
            if (!storeDirectory.exists() && !storeDirectory.mkdir())
                stopSelf()
        }

        CoroutineScope(Dispatchers.IO).launch {
            Looper.prepare()
            handler = Handler(Looper.getMainLooper())
            Looper.loop()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.extras?.getSerializable(Extras.ACTION, ActionValues::class.java)
        } else {
            intent?.extras?.getSerializable(Extras.ACTION) as ActionValues
        }

        when (action) {
            ActionValues.START -> {
                val channelId = "001"
                val channelName = "myChannel"
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                val notification = Notification.Builder(applicationContext, channelId)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .setContentTitle(getString(R.string.ClickToCancel))
//                    .setContentIntent(contentIntent)
                    .build()

                startForeground(154, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)

                val resultCode = intent!!.extras!!.getInt(Extras.RESULT_CODE, Activity.RESULT_CANCELED)
                val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.extras!!.getParcelable(Extras.DATA, Intent::class.java)
                } else {
                    intent.extras!!.getParcelable(Extras.DATA) as? Intent
                }

                data?.let { startProjection(resultCode, it) }
            }

            ActionValues.STOP -> {
                stopProjection()
                stopSelf()
            }

            null -> stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun startProjection(resultCode: Int, data: Intent) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mediaProjection == null)
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)

            mediaProjection?.let { projection ->
                with (resources.displayMetrics) {
                    mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        MediaRecorder(applicationContext)
                    else MediaRecorder()

                    mediaRecorder?.let { recorder ->
                        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
                        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT)
                        recorder.setOutputFile(path + "video.3gpp")
                        recorder.prepare()

                        projection.registerCallback(object : MediaProjection.Callback() {}, handler)

                        virtualDisplay = projection.createVirtualDisplay(
                            VIRTUAL_DISPLAY_NAME,
                            widthPixels,
                            heightPixels,
                            densityDpi,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                            recorder.surface,
                            object : VirtualDisplay.Callback() {},
                            handler
                        )

                        recorder.start()
                    }
                }
            }
        }, 500)
    }

    private fun stopProjection() {
        mediaRecorder?.stop()
        mediaProjection?.stop()
        virtualDisplay?.release()
    }

    enum class ActionValues {
        START, STOP
    }

    object Extras {
        const val ACTION = "action"
        const val DATA = "data"
        const val RESULT_CODE = "resultCode"
    }

    companion object {
        private const val VIRTUAL_DISPLAY_NAME = "ScreenCapture"
    }
}