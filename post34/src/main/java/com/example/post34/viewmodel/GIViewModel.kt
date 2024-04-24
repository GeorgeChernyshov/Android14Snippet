package com.example.post34.viewmodel

import androidx.lifecycle.ViewModel
import com.example.post34.uistate.GIScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GIViewModel : ViewModel() {

    private val _state = MutableStateFlow(GIScreenUiState.DEFAULT)
    val state = _state.asStateFlow()

    fun setOption(option: GIScreenUiState.GenderOption) {
        _state.value = GIScreenUiState(option)
    }
}