package com.example.post34.uistate

data class ScreenSharingUiState(
    val isSharing: Boolean
) {
    companion object {
        val DEFAULT = ScreenSharingUiState(false)
    }
}