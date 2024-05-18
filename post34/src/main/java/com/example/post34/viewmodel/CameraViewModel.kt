package com.example.post34.viewmodel

import androidx.lifecycle.ViewModel
import com.example.post34.uistate.CameraScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {

    private val _state = MutableStateFlow(CameraScreenUiState.DEFAULT)
    val state = _state.asStateFlow()

    fun setExtensionStatus(extensionStatus: CameraScreenUiState.ExtensionStatus) {
        _state.value = state.value.copy(extensionStatus = extensionStatus)
    }

    fun setCaptureLatency(latency: String) {
        _state.value = state.value.copy(captureLatency = latency)
    }

    fun setProcessingLatency(latency: String) {
        _state.value = state.value.copy(processingLatency = latency)
    }
}