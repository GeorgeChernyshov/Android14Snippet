package com.example.post34.uistate

data class CameraScreenUiState(
    val extensionStatus: ExtensionStatus,
    val captureLatency: String? = null,
    val processingLatency: String? = null
) {
    enum class ExtensionStatus {
        UNKNOWN, AVAILABLE, NOT_AVAILABLE
    }

    companion object {
        val DEFAULT = CameraScreenUiState(
            extensionStatus = ExtensionStatus.UNKNOWN,
            captureLatency = null,
            processingLatency = null
        )
    }
}