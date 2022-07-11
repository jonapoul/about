package com.jonapoul.logs.data

import com.jonapoul.about.di.AboutResources
import java.io.File
import javax.inject.Inject

internal class LogDirectory @Inject constructor(
    private val aboutResources: AboutResources,
) {
    fun getDirectory(): File =
        aboutResources.logDirectory

    fun getLogFiles(): List<File> =
        getDirectory()
            .listFiles()
            ?.filter { it.extension == LOG_FILE_EXTENSION }
            ?: emptyList()

    private companion object {
        const val LOG_FILE_EXTENSION = "log"
    }
}
