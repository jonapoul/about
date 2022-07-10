package com.jonapoul.about.domain

import android.content.Context
import com.jonapoul.about.R
import com.jonapoul.about.di.AboutResources
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.domain.TextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AboutTextCreator @Inject constructor(
    @ApplicationContext context: Context,
    aboutResources: AboutResources,
    buildConfig: IBuildConfig,
) : TextCreator(context) {

    val noBrowserFound: String =
        fromRes(R.string.about_no_browser_found)

    val releaseNoneFound: String =
        fromRes(R.string.about_latest_release_none_found)

    val releaseNoUpdate: String =
        fromRes(R.string.about_latest_release_no_update)

    val reportIssueUrl: String =
        aboutResources.githubIssuesUrl

    val sourceCodeUrl: String =
        aboutResources.githubUrl

    val developedBy: String =
        context.getString(
            R.string.about_developed_by,
            aboutResources.developerName,
            aboutResources.developmentYear,
        )

    val softwareLicense: String =
        aboutResources.softwareLicense

    val version: String =
        context.getString(
            R.string.about_version_placeholder,
            buildConfig.versionName,
            buildConfig.versionCode,
        )
}
