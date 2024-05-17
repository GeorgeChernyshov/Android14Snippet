package com.example.post34.viewmodel

import androidx.lifecycle.ViewModel
import com.example.post34.uistate.ScreenSharingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScreenSharingViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScreenSharingUiState.DEFAULT)
    val state = _state.asStateFlow()

    fun setSharing(isSharing: Boolean) {
        _state.value = state.value.copy(isSharing = isSharing)
    }
}