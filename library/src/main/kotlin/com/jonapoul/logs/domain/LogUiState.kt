package com.jonapoul.logs.domain

internal sealed class LogUiState {
    data class Failure(
        val message: String,
    ) : LogUiState()

    data class Success(
        val fileCount: Int,
        val fileSize: String,
        val loggingSince: String,
    ) : LogUiState()
}
