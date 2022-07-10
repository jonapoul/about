package com.jonapoul.about.data

import com.jonapoul.about.di.AboutResources
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.data.api.ApiResult
import com.jonapoul.common.di.IODispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant

internal class GithubRepository @Inject constructor(
    @IODispatcher private val io: CoroutineDispatcher,
    private val api: GithubApi,
    private val aboutResources: AboutResources,
    private val buildConfig: IBuildConfig,
) {
    fun getLatestRelease(): Flow<LatestReleaseState> = flow {
        emit(LatestReleaseState.Loading)
        val state = withContext(io) {
            when (val result = ApiResult.from { api.getAppReleases(aboutResources.githubReleasesUrl) }) {
                is ApiResult.Success -> getSuccessState(result.data)
                is ApiResult.Failure -> LatestReleaseState.Failure(result.message)
            }
        }
        emit(state)
    }

    private fun getSuccessState(releases: List<GithubReleaseModel>): LatestReleaseState {
        val latest = releases.maxByOrNull { it.publishedAt }
        return when {
            latest == null -> {
                /* Returned an empty list, no releases are available */
                LatestReleaseState.NoReleases
            }
            buildConfig.versionName == latest.versionName -> {
                /* Same version as this one */
                LatestReleaseState.NoUpdate
            }
            else -> {
                /* We have a release, so compare its timestamp to this app */
                val releaseInstant = Instant.parse(latest.publishedAt)
                if (releaseInstant.isAfter(buildConfig.buildTime)) {
                    /* The release is newer than this installed version */
                    LatestReleaseState.UpdateAvailable(latest)
                } else {
                    /* It's the same version as current (or earlier?) */
                    LatestReleaseState.NoUpdate
                }
            }
        }
    }
}
