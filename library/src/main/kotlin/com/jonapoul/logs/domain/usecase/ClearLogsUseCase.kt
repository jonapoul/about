package com.jonapoul.logs.domain.usecase

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jonapoul.common.domain.Toaster
import com.jonapoul.logs.data.LogDirectory
import com.jonapoul.logs.domain.ExportLogsTextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ClearLogsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logDirectory: LogDirectory,
    private val textCreator: ExportLogsTextCreator,
    private val toaster: Toaster,
) {
    /**
     * a) delete all log files
     * b) show a toast to show how many files we've deleted
     * c) restart the app process
     */
    fun clearLogs() {
        val files = logDirectory.getLogFiles()
        val size = files.size
        files.forEach { it.delete() }
        toaster.toast(textCreator.deleted(size))
        ProcessPhoenix.triggerRebirth(context)
    }
}
