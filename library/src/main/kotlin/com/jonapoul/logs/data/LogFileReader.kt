package com.jonapoul.logs.data

import java.io.File
import javax.inject.Inject

/**
 * Extracted from [LogStateRepository] to help with unit testing.
 */
internal class LogFileReader @Inject constructor() {
    fun readFirstLine(file: File): String =
        file.bufferedReader().use { it.readLine() }
}
