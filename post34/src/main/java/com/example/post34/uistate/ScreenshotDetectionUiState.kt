package com.example.post34.uistate

data class ScreenshotDetectionUiState(
    val isTaken: Boolean
) {
    companion object {
        val DEFAULT = ScreenshotDetectionUiState(false)
    }
}