package com.jonapoul.libraries.domain

import app.cash.turbine.test
import com.jonapoul.common.test.CoroutineRule
import com.jonapoul.libraries.data.LibrariesLoadedState
import com.jonapoul.libraries.data.LicensesRepository
import com.jonapoul.libraries.data.db.LibraryEntity
import com.jonapoul.libraries.domain.usecase.ObserveLibrariesUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class ObserveLibrariesUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineRule = CoroutineRule()

    @MockK
    private lateinit var repo: LicensesRepository

    @RelaxedMockK
    private lateinit var textCreator: LibrariesTextCreator

    private lateinit var useCase: ObserveLibrariesUseCase

    @Before
    fun before() {
        useCase = ObserveLibrariesUseCase(repo, textCreator)
    }

    @Test
    fun `Valid libraries returns success`() = runTest {
        /* Given */
        coEvery { repo.getOpenSourceLicenses() } returns VALID_ENTITIES

        /* When */
        useCase.flow.test {
            /* Then */
            assertEquals(expected = LibrariesLoadedState.Loading, actual = awaitItem())
            assertEquals(expected = LibrariesLoadedState.Success(VALID_ENTITIES), actual = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Empty libraries returns failure`() = runTest {
        /* Given */
        coEvery { repo.getOpenSourceLicenses() } returns emptyList()
        val noneFound = "none found"
        every { textCreator.noLibrariesFound } returns noneFound

        /* When */
        useCase.flow.test {
            /* Then */
            assertEquals(expected = LibrariesLoadedState.Loading, actual = awaitItem())
            assertEquals(expected = LibrariesLoadedState.Failure(noneFound), actual = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Repo Exception returns failure`() = runTest {
        /* Given */
        val message = "an expected exception happened!"
        coEvery { repo.getOpenSourceLicenses() } throws Exception(message)
        every { textCreator.failedLoadingLibraries(any()) } returns message

        /* When */
        useCase.flow.test {
            /* Then */
            assertEquals(expected = LibrariesLoadedState.Loading, actual = awaitItem())
            assertEquals(expected = LibrariesLoadedState.Failure(message), actual = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        val VALID_ENTITIES = listOf(
            LibraryEntity(
                appVersionCode = 1,
                project = "project",
                description = "description",
                version = "version",
                authors = "authors",
                url = "http://www.example.com",
                license = "license",
                artifact = "artifact"
            )
        )
    }
}
