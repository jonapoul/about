package com.jonapoul.logs.domain

import com.jonapoul.about.R
import com.jonapoul.about.di.AboutResources
import com.jonapoul.common.data.localised
import com.jonapoul.common.di.DefaultDispatcher
import com.jonapoul.common.domain.Toaster
import com.jonapoul.logs.data.LogDirectory
import com.jonapoul.logs.data.model.ZipFilesState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
internal class FileZipper @Inject constructor(
    @DefaultDispatcher private val default: CoroutineDispatcher,
    private val toaster: Toaster,
    private val logDirectory: LogDirectory,
    private val aboutResources: AboutResources,
) {
    fun zipFiles(files: List<File>): Flow<ZipFilesState> = flow {
        emit(ZipFilesState.Loading)
        try {
            emitZippedFile(files)
        } catch (e: Exception) {
            Timber.e(e)
            emit(ZipFilesState.Failure(e))
        }
    }.flowOn(default)

    private suspend fun FlowCollector<ZipFilesState>.emitZippedFile(files: List<File>) {
        val outputFile = getOutputFile()
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile)))
            .use { zipOutputStream ->
                files.forEach { file ->
                    FileInputStream(file).use { inputStream ->
                        zipOutputStream.putNextEntry(ZipEntry(file.name))
                        inputStream.copyTo(zipOutputStream)
                    }
                }
            }
        val state = ZipFilesState.Success(
            outputFile,
            suggestedFilename = outputFile.name
        )
        toaster.coToast(R.string.logs_choose_output_file)
        emit(state)
    }

    private fun getOutputFile(): File =
        File(
            logDirectory.getDirectory(),
            aboutResources.logZipFilename(timestamp = FORMATTER.format(Instant.now()))
        )

    private companion object {
        val FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .localised()
    }
}
