package com.jonapoul.logs.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonapoul.logs.data.model.ZipFilesState
import com.jonapoul.logs.domain.LogUiState
import com.jonapoul.logs.domain.usecase.ClearLogsUseCase
import com.jonapoul.logs.domain.usecase.CopyFileUseCase
import com.jonapoul.logs.domain.usecase.LogStateUseCase
import com.jonapoul.logs.domain.usecase.ZipLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class LogsViewModel @Inject constructor(
    private val logStateUseCase: LogStateUseCase,
    private val zipLogsUseCase: ZipLogsUseCase,
    private val copyFileUseCase: CopyFileUseCase,
    private val clearLogsUseCase: ClearLogsUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch {
            while (true) {
                logStateUseCase.updateState()
                delay(UPDATE_STATE_PERIOD)
            }
        }
    }

    val logState: Flow<LogUiState>
        get() = logStateUseCase.logUiState

    fun zipLogs(): Flow<ZipFilesState> =
        zipLogsUseCase.zipLogs()

    fun copyZipFile(uri: Uri?, zipFile: File?) {
        Timber.d("copyZipFile $uri $zipFile")
        if (zipFile != null) {
            viewModelScope.launch {
                copyFileUseCase.copyFile(input = zipFile, output = uri)
            }
        }
    }

    fun clearLogs() {
        clearLogsUseCase.clearLogs()
    }

    private companion object {
        val UPDATE_STATE_PERIOD = 10.seconds
    }
}
