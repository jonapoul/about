package com.jonapoul.logs.domain.usecase

import android.content.Context
import android.net.Uri
import com.jonapoul.common.di.IODispatcher
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import com.jonapoul.logs.domain.ExportLogsTextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

internal class CopyFileUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IODispatcher private val io: CoroutineDispatcher,
    private val snackbarFeed: SnackbarFeed,
    private val textCreator: ExportLogsTextCreator,
) {
    suspend fun copyFile(input: File, output: Uri?) {
        try {
            if (output == null) {
                throw NullPointerException("null output URI!")
            }
            performCopy(input, output)
            snackbarFeed.add(
                SnackbarMessage.Success(textCreator.succeededExporting)
            )
        } catch (e: Exception) {
            snackbarFeed.add(
                SnackbarMessage.Warning(
                    textCreator.failedExporting(e)
                )
            )
        } finally {
            /* Delete the zipped file once we're finished, regardless of the result */
            input.delete()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun performCopy(input: File, output: Uri) {
        withContext(io) {
            context.contentResolver.openOutputStream(output)?.use { outputStream ->
                FileInputStream(input).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }
}
