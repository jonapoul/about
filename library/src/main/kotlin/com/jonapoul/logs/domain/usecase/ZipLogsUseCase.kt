package com.jonapoul.logs.domain.usecase

import com.jonapoul.logs.data.LogDirectory
import com.jonapoul.logs.data.model.ZipFilesState
import com.jonapoul.logs.domain.FileZipper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ZipLogsUseCase @Inject constructor(
    private val logDirectory: LogDirectory,
    private val fileZipper: FileZipper,
) {
    fun zipLogs(): Flow<ZipFilesState> {
        val files = logDirectory.getLogFiles()
        return fileZipper.zipFiles(files)
    }
}
