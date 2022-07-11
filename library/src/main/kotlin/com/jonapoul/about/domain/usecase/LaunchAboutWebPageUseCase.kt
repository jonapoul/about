package com.jonapoul.about.domain.usecase

import android.content.Context
import com.jonapoul.about.di.AboutResources
import com.jonapoul.about.domain.AboutTextCreator
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import com.jonapoul.internal.launchWebPage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LaunchAboutWebPageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val snackbarFeed: SnackbarFeed,
    private val textCreator: AboutTextCreator,
    private val aboutResources: AboutResources,
) {
    suspend fun launchSourceCode() {
        launch(aboutResources.githubUrl)
    }

    suspend fun launchReportIssues() {
        launch(aboutResources.githubIssuesUrl)
    }

    suspend fun launch(url: String) {
        launchWebPage(context, url) {
            snackbarFeed.add(
                SnackbarMessage.Warning(textCreator.noBrowserFound)
            )
        }
    }
}
