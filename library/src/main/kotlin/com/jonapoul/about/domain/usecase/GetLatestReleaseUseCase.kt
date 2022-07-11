package com.jonapoul.about.domain.usecase

import com.jonapoul.about.data.GithubRepository
import com.jonapoul.about.data.LatestReleaseState
import com.jonapoul.about.domain.AboutTextCreator
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class GetLatestReleaseUseCase @Inject constructor(
    private val githubRepository: GithubRepository,
    private val snackbarFeed: SnackbarFeed,
    private val textCreator: AboutTextCreator,
) {
    fun getLatestRelease(): Flow<LatestReleaseState> =
        githubRepository.getLatestRelease()
            .onEach { showSnackbarOnFailure(it) }

    private suspend fun showSnackbarOnFailure(state: LatestReleaseState) {
        snackbarFeed.add(
            when (state) {
                is LatestReleaseState.Failure ->
                    SnackbarMessage.Warning(state.message)
                LatestReleaseState.NoUpdate ->
                    SnackbarMessage.Caution(textCreator.releaseNoUpdate)
                LatestReleaseState.NoReleases ->
                    SnackbarMessage.Caution(textCreator.releaseNoneFound)
                else ->
                    return
            }
        )
    }
}
