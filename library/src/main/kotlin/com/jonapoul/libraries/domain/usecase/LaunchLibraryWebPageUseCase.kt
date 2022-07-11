package com.jonapoul.libraries.domain.usecase

import android.content.Context
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import com.jonapoul.internal.launchWebPage
import com.jonapoul.libraries.domain.LibrariesTextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LaunchLibraryWebPageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val snackbarFeed: SnackbarFeed,
    private val textCreator: LibrariesTextCreator,
) {
    suspend fun launch(url: String) {
        launchWebPage(context, url) {
            snackbarFeed.add(
                SnackbarMessage.Warning(textCreator.noBrowserFound)
            )
        }
    }
}
