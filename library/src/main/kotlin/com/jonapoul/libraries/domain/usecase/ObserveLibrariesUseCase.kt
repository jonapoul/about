package com.jonapoul.libraries.domain.usecase

import com.jonapoul.common.core.requireMessage
import com.jonapoul.common.domain.usecase.ObservableUseCase
import com.jonapoul.libraries.data.LibrariesLoadedState
import com.jonapoul.libraries.data.LicensesRepository
import com.jonapoul.libraries.domain.LibrariesTextCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ObserveLibrariesUseCase @Inject constructor(
    private val licensesRepository: LicensesRepository,
    private val textCreator: LibrariesTextCreator,
) : ObservableUseCase<LibrariesLoadedState>() {
    override fun fetchData(): Flow<LibrariesLoadedState> = flow {
        emit(LibrariesLoadedState.Loading)
        try {
            val entities = licensesRepository.getOpenSourceLicenses()
            if (entities.isEmpty()) {
                emit(LibrariesLoadedState.Failure(textCreator.noLibrariesFound))
            } else {
                emit(LibrariesLoadedState.Success(entities))
            }
        } catch (e: Exception) {
            val message = textCreator.failedLoadingLibraries(e.requireMessage())
            emit(LibrariesLoadedState.Failure(message))
        }
    }
}
