package com.example.post34.viewmodel

import androidx.lifecycle.ViewModel
import com.example.post34.uistate.ScreenshotDetectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScreenshotDetectionViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScreenshotDetectionUiState.DEFAULT)
    val state = _state.asStateFlow()

    fun setTaken(taken: Boolean) {
        _state.value = ScreenshotDetectionUiState(taken)
    }
}