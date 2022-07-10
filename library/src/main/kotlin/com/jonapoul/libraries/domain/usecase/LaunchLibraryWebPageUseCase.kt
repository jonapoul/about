package com.jonapoul.libraries.domain.usecase

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import com.jonapoul.libraries.domain.LibrariesTextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LaunchLibraryWebPageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val snackbarFeed: SnackbarFeed,
    private val textCreator: LibrariesTextCreator,
) {
    suspend fun launch(url: String) {
        /* Open a web browser to the page, or another app if one is installed */
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        try {
            ContextCompat.startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            snackbarFeed.add(
                SnackbarMessage.Warning(textCreator.noBrowserFound)
            )
        }
    }
}
