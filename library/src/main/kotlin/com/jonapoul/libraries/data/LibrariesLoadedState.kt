package com.jonapoul.libraries.data

import com.jonapoul.libraries.data.db.LibraryEntity

internal sealed class LibrariesLoadedState {
    object Loading : LibrariesLoadedState()
    data class Success(val libraries: List<LibraryEntity>) : LibrariesLoadedState()
    data class Failure(val message: String) : LibrariesLoadedState()
}
