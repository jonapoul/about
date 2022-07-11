package com.jonapoul.about.sample

import android.content.Context
import com.jonapoul.about.di.AboutResources
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

@Suppress("MagicNumber")
class SampleAboutResources @Inject constructor(
    @ApplicationContext private val context: Context,
) : AboutResources {
    override val logDirectory: File =
        File(context.dataDir, "logs")

    override val appName: Int =
        R.string.app_name

    override val appIconResource: Int =
        R.mipmap.ic_launcher_round

    override val githubReleasesUrl: String =
        "/repos/jonapoul/about/releases"

    override val githubIssuesUrl: String =
        "https://github.com/jonapoul/about/issues/new"

    override val githubUrl: String =
        "https://github.com/jonapoul/about"

    override val developerName: String =
        "Jon Poulton"

    override val developmentYear: Int =
        2022

    override val softwareLicense: String =
        "Apache 2.0"

    override val logDescriptionText: String =
        context.getString(R.string.log_description_text)

    override val showLogsButton: Boolean =
        false

    override fun readLicensesJsonString(): String =
        context.assets
            .open(LICENSES_ASSET_FILENAME)
            .reader()
            .use { it.readText() }

    override fun logZipFilename(timestamp: String): String =
        "about_logs_$timestamp.zip"

    private companion object {
        const val LICENSES_ASSET_FILENAME = "open_source_licenses.json"
    }
}
