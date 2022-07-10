package com.jonapoul.logs.domain.usecase

import com.jonapoul.common.core.requireMessage
import com.jonapoul.logs.data.LogStateRepository
import com.jonapoul.logs.data.model.LogState
import com.jonapoul.logs.domain.ExportLogsTextCreator
import com.jonapoul.logs.domain.FileSizeFormatter
import com.jonapoul.logs.domain.LogUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import timber.log.Timber
import javax.inject.Inject

internal class LogStateUseCase @Inject constructor(
    private val logStateRepository: LogStateRepository,
    private val fileSizeFormatter: FileSizeFormatter,
    private val textCreator: ExportLogsTextCreator,
) {
    val logUiState: Flow<LogUiState>
        get() = logStateRepository.logState.map {
            when (it) {
                is LogState.Failure -> getFailure(it)
                is LogState.Success -> getSuccess(it)
            }
        }
            .onEach { Timber.d("Updated state: $it") }

    private fun getFailure(state: LogState.Failure): LogUiState =
        LogUiState.Failure(state.exception.requireMessage())

    private fun getSuccess(state: LogState.Success): LogUiState =
        LogUiState.Success(
            loggingSince = getLoggingSince(state.loggingSince),
            fileSize = fileSizeFormatter.bytesToString(state.fileSizeBytes, decimalPlaces = 1),
            fileCount = state.files.size
        )

    private fun getLoggingSince(instant: Instant?): String =
        if (instant == null) {
            " - "
        } else {
            val duration = Duration.between(instant, Instant.now())
            textCreator.formatDuration(duration)
        }

    suspend fun updateState() {
        logStateRepository.updateState()
    }
}
