package com.example.post34.screen.ux

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.post34.R
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen

@Composable
fun UserExperienceScreen(onNextClicked: () -> Unit) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.UserExperience.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                PartialAccessBlock()
                DismissNotificationBlock(Modifier.padding(top = 16.dp))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    InformationBlock(Modifier.padding(top = 16.dp))


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

@Composable
fun PartialAccessBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        Toast.makeText(
            context,
            "You picked a photo, good job!",
            Toast.LENGTH_SHORT
        ).show()
    }

    Column(modifier) {
        Text(text = stringResource(id = R.string.user_experience_photo_title))

        Button(
            onClick = { pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            ) }
        ) {
            Text(text = stringResource(id = R.string.user_experience_photo_button))
        }

        Text(
            text = stringResource(
                id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    R.string.user_experience_photo_hint_some
                else R.string.user_experience_photo_hint_all
            )
        )
    }
}

@Composable
fun DismissNotificationBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(modifier) {
        Button(onClick = {
            NotificationHelper(context).showNotification()
        }) {
            Text(text = stringResource(id = R.string.user_experience_notification_button))
        }
        
        Text(
            text = stringResource(
                id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    R.string.user_experience_notification_hint_dismiss
                else R.string.user_experience_notification_hint_non_dismiss
            )
        )
    }
}

@Composable
fun InformationBlock(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = stringResource(id = R.string.user_experience_info_data))
        Text(text = stringResource(id = R.string.user_experience_info_font))
        Text(text = stringResource(id = R.string.user_experience_predictive_back))
    }
}

@Preview(showBackground = true)
@Composable
fun PartialAccessBlockPreview() {
    PartialAccessBlock()
}

@Preview(showBackground = true)
@Composable
fun DismissNotificationBlockPreview() {
    DismissNotificationBlock()
}

@Preview
@Composable
fun InformationBlockPreview() {
    InformationBlock()
}