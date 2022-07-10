package com.jonapoul.about.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.jonapoul.about.R
import com.jonapoul.about.data.AboutState
import com.jonapoul.about.data.GithubReleaseModel
import com.jonapoul.about.data.LatestReleaseState
import com.jonapoul.about.databinding.FragmentAboutBinding
import com.jonapoul.common.domain.safelyNavigate
import com.jonapoul.common.ui.CommonFragment
import com.jonapoul.common.ui.collectFlow
import com.jonapoul.common.ui.dialogs.setSimpleNegativeButton
import com.jonapoul.common.ui.dialogs.setSimplePositiveButton
import com.jonapoul.common.ui.dialogs.showLoadingDialog
import com.jonapoul.common.ui.dialogs.showSuccessDialog
import com.jonapoul.common.ui.view.showIfTrue
import com.jonapoul.common.ui.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : CommonFragment(R.layout.fragment_about, menu = null) {
    override val binding by viewBinding(FragmentAboutBinding::bind)
    private val viewModel by viewModels<AboutViewModel>()

    private var latestReleaseDialog: AlertDialog? = null
    private var refreshingUpdatesDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseHeader()
        initialiseButtons()
        collectFlow(viewModel.aboutState, ::onBuildState)
        collectFlow(viewModel.latestReleaseState, ::onReleaseState)
    }

    private fun initialiseHeader() {
        with(binding.aboutHeader) {
            sourceCodeButton.setOnClickListener { viewModel.viewSourceCode() }
        }
    }

    private fun initialiseButtons() {
        with(binding.aboutButtons) {
            reportIssueButton.setOnClickListener { viewModel.reportIssue() }
            checkUpdatesButton.setOnClickListener { viewModel.checkForUpdates() }
            licensesButton.setOnClickListener { navController.safelyNavigate(AboutFragmentDirections.toLibraries()) }
            logsButton.setOnClickListener { navController.safelyNavigate(AboutFragmentDirections.toLogs()) }
        }
    }

    private fun onBuildState(state: AboutState) {
        with(binding.aboutBuild) {
            aboutVersion.setSubtitle(state.versionInfo)
            aboutGitId.setSubtitle(state.gitId)
            aboutBuildTime.setSubtitle(state.buildTimestamp)
            aboutInstallTime.setSubtitle(state.installTimestamp)
        }
        with(binding.aboutHeader) {
            appName.setText(state.appName)
            developedBy.text = state.developedBy
            appLicense.text = state.softwareLicense
            appIcon.setImageResource(state.appIconResource)
        }
        with(binding.aboutButtons) {
            logsButton.showIfTrue(state.showLogsButton)
        }
    }

    private fun onReleaseState(state: LatestReleaseState) {
        /* Show/hide the loading dialog */
        refreshingUpdatesDialog = if (state is LatestReleaseState.Loading) {
            onLoading()
        } else {
            refreshingUpdatesDialog?.dismiss()
            null
        }

        /* Show/hide the result dialog */
        latestReleaseDialog = if (state is LatestReleaseState.UpdateAvailable) {
            onUpdateAvailable(state.release)
        } else {
            latestReleaseDialog?.dismiss()
            null
        }
    }

    private fun onLoading(): AlertDialog =
        showLoadingDialog(R.string.about_check_updates_loading) {
            setCancelable(false)
        }

    private fun onUpdateAvailable(release: GithubReleaseModel): AlertDialog {
        val message = getString(R.string.about_latest_release_found_msg, release.versionName)
        val title = getString(R.string.about_latest_release_found_title)
        return showSuccessDialog(message, title) {
            setSimplePositiveButton { viewModel.viewLatestRelease(release) }
            setSimpleNegativeButton { viewModel.clearUpdateState() }
        }
    }
}
