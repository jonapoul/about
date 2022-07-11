package com.jonapoul.logs.data.model

import org.threeten.bp.Instant
import java.io.File

internal sealed class LogState {
    data class Failure(
        val exception: Exception,
    ) : LogState()

    data class Success(
        val loggingSince: Instant?,
        val fileSizeBytes: Long,
        val files: List<File>,
    ) : LogState()
}
