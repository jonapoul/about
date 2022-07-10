package com.jonapoul.about.data

internal sealed class LatestReleaseState {
    object Inactive : LatestReleaseState()
    object Loading : LatestReleaseState()

    object NoUpdate : LatestReleaseState()
    data class UpdateAvailable(val release: GithubReleaseModel) : LatestReleaseState()
    object NoReleases : LatestReleaseState()
    data class Failure(val message: String) : LatestReleaseState()
}
