package com.jonapoul.logs.data.model

import java.io.File

internal sealed class ZipFilesState {
    object Loading : ZipFilesState()
    data class Failure(val exception: Exception) : ZipFilesState()
    data class Success(val zipFile: File, val suggestedFilename: String) : ZipFilesState()
}
