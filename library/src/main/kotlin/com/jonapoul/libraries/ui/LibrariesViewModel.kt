package com.jonapoul.libraries.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonapoul.libraries.data.LibrariesLoadedState
import com.jonapoul.libraries.domain.usecase.LaunchLibraryWebPageUseCase
import com.jonapoul.libraries.domain.usecase.ObserveLibrariesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LibrariesViewModel @Inject constructor(
    private val observeLibrariesUseCase: ObserveLibrariesUseCase,
    private val launchWebPageUseCase: LaunchLibraryWebPageUseCase,
) : ViewModel() {

    val librariesLoadedState: Flow<LibrariesLoadedState> =
        observeLibrariesUseCase.flow

    init {
        viewModelScope.launch {
            observeLibrariesUseCase.launch()
        }
    }

    fun launchUrl(url: String?) {
        if (url != null) {
            viewModelScope.launch {
                launchWebPageUseCase.launch(url)
            }
        }
    }
}
