package com.jonapoul.about.domain.usecase

import android.content.Context
import com.jonapoul.about.data.AboutState
import com.jonapoul.about.di.AboutResources
import com.jonapoul.about.domain.AboutTextCreator
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.data.localised
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

internal class ObserveAboutDataUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val textCreator: AboutTextCreator,
    private val aboutResources: AboutResources,
    private val buildConfig: IBuildConfig,
) {
    val buildState: Flow<AboutState>
        get() = flowOf(
            AboutState(
                versionInfo = textCreator.version,
                gitId = buildConfig.gitId,
                buildTimestamp = dateTimeFormatter.format(buildConfig.buildTime),
                installTimestamp = dateTimeFormatter.format(installTime()),
                appName = aboutResources.appName,
                developedBy = textCreator.developedBy,
                softwareLicense = aboutResources.softwareLicense,
                appIconResource = aboutResources.appIconResource,
                showLogsButton = aboutResources.showLogsButton,
            )
        )

    private fun installTime(): Instant {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return Instant.ofEpochMilli(packageInfo.firstInstallTime)
    }

    private val dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm:ss, EE dd MMM yyyy Z")
            .localised()
}
