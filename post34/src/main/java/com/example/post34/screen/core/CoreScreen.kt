package com.example.post34.screen.core

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.R
import java.util.Date

@Composable
fun CoreScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Core.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                AlarmBlock()
                KillBackgroundProcessBlock(modifier = Modifier.padding(top = 16.dp))
            }
        }
    )
}

@Composable
fun AlarmBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(AlarmManager::class.java)

    Column(modifier) {
        Button(onClick = {
            if (
                Build.VERSION.SDK_INT < Build.VERSION_CODES.S
                    || alarmManager.canScheduleExactAlarms()
            ) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        Date().time + 1000L,
                        PendingIntent.getActivity(context, 11, Intent(Intent.ACTION_PICK), PendingIntent.FLAG_IMMUTABLE)
                    ),
                    PendingIntent.getActivity(context, 11, Intent(Intent.ACTION_PICK), PendingIntent.FLAG_IMMUTABLE)
                )
            }
        }) {
            Text(text = stringResource(id = R.string.core_alarm_start))
        }
        
        Text(text = stringResource(
            id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                R.string.core_alarm_hint_restrict
            else R.string.core_alarm_hint_allow
        ))
    }
}

@Composable
fun KillBackgroundProcessBlock(
    modifier: Modifier = Modifier
) {
    val activityManager = LocalContext.current.getSystemService(ActivityManager::class.java)

    Column(modifier) {
        Text(text = stringResource(id = R.string.core_kill_background_process_title))

        Button(onClick = {
            activityManager.killBackgroundProcesses("com.example.post31")
        }) {
            Text(text = stringResource(id = R.string.core_kill_background_process_button))
        }

        Text(
            text = stringResource(
                id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    R.string.core_kill_background_process_restrict
                else R.string.core_kill_background_process_allow
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmBlockPreview() {
    AlarmBlock()
}

@Preview(showBackground = true)
@Composable
fun KillBackgroundProcessBlockPreview() {
    KillBackgroundProcessBlock()
}