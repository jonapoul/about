package com.jonapoul.about.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonapoul.about.data.AboutState
import com.jonapoul.about.data.GithubReleaseModel
import com.jonapoul.about.data.LatestReleaseState
import com.jonapoul.about.domain.usecase.GetLatestReleaseUseCase
import com.jonapoul.about.domain.usecase.LaunchAboutWebPageUseCase
import com.jonapoul.about.domain.usecase.ObserveAboutDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class AboutViewModel @Inject constructor(
    observeAboutDataUseCase: ObserveAboutDataUseCase,
    private val getLatestReleaseUseCase: GetLatestReleaseUseCase,
    private val launchWebPageUseCase: LaunchAboutWebPageUseCase,
) : ViewModel() {

    private val _latestReleaseState = MutableStateFlow<LatestReleaseState>(LatestReleaseState.Inactive)

    val latestReleaseState: Flow<LatestReleaseState> =
        _latestReleaseState.asStateFlow()

    val aboutState: Flow<AboutState> =
        observeAboutDataUseCase.buildState

    fun viewLatestRelease(release: GithubReleaseModel) {
        viewModelScope.launch {
            launchWebPageUseCase.launch(release.htmlUrl)
        }
    }

    fun viewSourceCode() {
        viewModelScope.launch {
            launchWebPageUseCase.launchSourceCode()
        }
    }

    fun reportIssue() {
        viewModelScope.launch {
            launchWebPageUseCase.launchReportIssues()
        }
    }

    fun checkForUpdates() {
        Timber.d("Checking for updates")
        viewModelScope.launch {
            getLatestReleaseUseCase.getLatestRelease()
                .collect { _latestReleaseState.value = it }
        }
    }

    fun clearUpdateState() {
        _latestReleaseState.value = LatestReleaseState.Inactive
    }
}
