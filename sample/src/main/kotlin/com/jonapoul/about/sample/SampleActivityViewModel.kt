package com.jonapoul.about.sample

import androidx.lifecycle.ViewModel
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SampleActivityViewModel @Inject constructor(
    snackbarFeed: SnackbarFeed,
) : ViewModel() {
    val snackbars: Flow<SnackbarMessage?> = snackbarFeed.snackbars
}
