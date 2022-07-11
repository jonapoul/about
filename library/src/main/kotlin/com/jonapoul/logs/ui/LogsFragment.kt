package com.jonapoul.logs.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.jonapoul.about.R
import com.jonapoul.about.databinding.FragmentLogsBinding
import com.jonapoul.common.core.requireMessage
import com.jonapoul.common.ui.CommonFragment
import com.jonapoul.common.ui.collectFlow
import com.jonapoul.common.ui.dialogs.setSimpleNegativeButton
import com.jonapoul.common.ui.dialogs.setSimplePositiveButton
import com.jonapoul.common.ui.dialogs.showCautionDialog
import com.jonapoul.common.ui.dialogs.showLoadingDialog
import com.jonapoul.common.ui.dialogs.showWarningDialog
import com.jonapoul.common.ui.view.hide
import com.jonapoul.common.ui.view.show
import com.jonapoul.common.ui.viewbinding.viewBinding
import com.jonapoul.logs.data.model.ZipFilesState
import com.jonapoul.logs.domain.CreateZipDocument
import com.jonapoul.logs.domain.LogUiState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
internal class LogsFragment : CommonFragment(R.layout.fragment_logs, menu = null) {
    override val binding by viewBinding(FragmentLogsBinding::bind)
    private val viewModel by viewModels<LogsViewModel>()

    private var loadingDialog: AlertDialog? = null
    private var zipFile: File? = null

    private val exportZipContract = registerForActivityResult(CreateZipDocument(), ::onExportZipUri)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseButtons()
        collectFlow(viewModel.logState, ::onLogState)
    }

    private fun initialiseButtons() {
        with(binding.buttons) {
            exportButton.setOnClickListener { zipLogs() }
            clearButton.setOnClickListener { confirmClearingLogs() }
        }
    }

    private fun onLogState(state: LogUiState) {
        when (state) {
            is LogUiState.Success -> onLogStateSuccess(state)
            is LogUiState.Failure -> onLogStateFailure(state)
        }
    }

    private fun onLogStateSuccess(state: LogUiState.Success) {
        with(binding.table) {
            table.show()
            failedText.hide()
            numLogFiles.text = state.fileCount.toString()
            logFileSize.text = state.fileSize
            loggingSince.text = state.loggingSince
        }
    }

    private fun onLogStateFailure(state: LogUiState.Failure) {
        with(binding.table) {
            table.hide()
            failedText.show()
            failedText.text = getString(R.string.log_table_failed, state.message)
        }
    }

    private fun zipLogs() {
        collectFlow(viewModel.zipLogs(), ::onZipFilesState)
    }

    private fun onZipFilesState(state: ZipFilesState) {
        Timber.d("onZipState $state")
        loadingDialog = when (state) {
            ZipFilesState.Loading -> {
                showLoadingDialog(loadingText = R.string.logs_zipping)
            }
            is ZipFilesState.Failure -> {
                loadingDialog?.dismiss()
                val message = getString(R.string.log_export_failure_dialog_message, state.exception.requireMessage())
                showWarningDialog(message)
                null
            }
            is ZipFilesState.Success -> {
                this.zipFile = state.zipFile
                exportZipContract.launch(state.suggestedFilename)
                loadingDialog?.dismiss()
                null
            }
        }
    }

    private fun confirmClearingLogs() {
        showCautionDialog(
            message = getString(R.string.log_clear_dialog_message),
            title = getString(R.string.log_clear_dialog_title),
        ) {
            setSimpleNegativeButton()
            setSimplePositiveButton { viewModel.clearLogs() }
        }
    }

    private fun onExportZipUri(uri: Uri?) {
        Timber.d("onExportZipUri $uri")
        viewModel.copyZipFile(uri, zipFile)
        zipFile = null
    }
}
