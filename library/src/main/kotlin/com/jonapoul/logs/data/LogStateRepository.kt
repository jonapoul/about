package com.jonapoul.logs.data

import com.jonapoul.common.di.DefaultDispatcher
import com.jonapoul.logs.data.model.LogState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import java.io.File
import javax.inject.Inject

internal class LogStateRepository @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val logDirectory: LogDirectory,
    private val logTimestampParser: LogTimestampParser,
    private val logFileReader: LogFileReader,
) {
    private val mutableState = MutableStateFlow<LogState?>(null)

    val logState: Flow<LogState> =
        mutableState.asStateFlow().filterNotNull()

    suspend fun updateState() {
        try {
            mutableState.value = withContext(defaultDispatcher) { getSuccessState() }
        } catch (e: Exception) {
            mutableState.value = LogState.Failure(e)
        }
    }

    private fun getSuccessState(): LogState.Success {
        val files = logDirectory.getLogFiles()
        return LogState.Success(
            loggingSince = loggingSince(files),
            fileSizeBytes = fileSizeBytes(files),
            files = files
        )
    }

    private fun loggingSince(files: List<File>): Instant? {
        val instants = files.mapNotNull {
            logTimestampParser.parseTimestampOrNull(
                line = logFileReader.readFirstLine(it)
            )
        }
        return instants.minOrNull()
    }

    private fun fileSizeBytes(files: List<File>): Long =
        files.sumOf { it.length() }
}
