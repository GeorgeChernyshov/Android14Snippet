package com.example.post34.screen.grammar

import android.app.GrammaticalInflectionManager
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post34.components.AppBar
import com.example.post34.navigation.Screen
import com.example.post34.R
import com.example.post34.uistate.GIScreenUiState
import com.example.post34.viewmodel.GIViewModel

@Composable
fun GIScreen(viewModel: GIViewModel = viewModel()) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Grammar.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val gIM = context.getSystemService(GrammaticalInflectionManager::class.java)

                    gIM.setRequestedApplicationGrammaticalGender(
                        when (state.value.option) {
                            GIScreenUiState.GenderOption.MALE -> Configuration.GRAMMATICAL_GENDER_MASCULINE
                            GIScreenUiState.GenderOption.FEMALE -> Configuration.GRAMMATICAL_GENDER_FEMININE
                            else -> Configuration.GRAMMATICAL_GENDER_NEUTRAL
                        }
                    )
                }

                GIScreenUiState.radioOptions.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (it == state.value.option),
                            onClick = { viewModel.setOption(it) }
                        )
                        
                        Text(text = it.gender)
                    }
                }

                Text(text = stringResource(id = R.string.grammar_title))
                Text(text = stringResource(id = R.string.grammar_gendered_text))
            }
        }
    )
}