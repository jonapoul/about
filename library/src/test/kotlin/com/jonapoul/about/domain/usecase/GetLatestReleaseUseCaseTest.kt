package com.jonapoul.about.domain.usecase

import app.cash.turbine.test
import com.jonapoul.about.data.GithubRepository
import com.jonapoul.about.data.LatestReleaseState
import com.jonapoul.about.domain.AboutTextCreator
import com.jonapoul.common.test.CoroutineRule
import com.jonapoul.common.ui.SnackbarFeed
import com.jonapoul.common.ui.SnackbarMessage
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class GetLatestReleaseUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineRule = CoroutineRule()

    @RelaxedMockK
    private lateinit var githubRepository: GithubRepository

    @RelaxedMockK
    private lateinit var snackbarFeed: SnackbarFeed

    @RelaxedMockK
    private lateinit var textCreator: AboutTextCreator

    private lateinit var useCase: GetLatestReleaseUseCase

    @Before
    fun before() {
        every { textCreator.releaseNoUpdate } returns NO_UPDATE
        every { textCreator.releaseNoneFound } returns NO_RELEASES
        useCase = GetLatestReleaseUseCase(
            githubRepository,
            snackbarFeed,
            textCreator,
        )
    }

    @Test
    fun `Success state just returns success`() = runTest {
        /* Given */
        val state = LatestReleaseState.UpdateAvailable(mockk())
        setFlowEmission(state)

        /* When */
        assertEmission(state)
        assertSnackbar(null)
    }

    @Test
    fun `No releases triggers caution snackbar`() = runTest {
        /* Given */
        val state = LatestReleaseState.NoReleases
        setFlowEmission(state)

        /* Then */
        assertEmission(state)
        assertSnackbar(SnackbarMessage.Caution(NO_RELEASES))
    }

    @Test
    fun `No new updates triggers caution snackbar`() = runTest {
        /* Given */
        val state = LatestReleaseState.NoUpdate
        setFlowEmission(state)

        /* Then */
        assertEmission(state)
        assertSnackbar(SnackbarMessage.Caution(NO_UPDATE))
    }

    @Test
    fun `Error triggers warning snackbar`() = runTest {
        /* Given */
        val state = LatestReleaseState.Failure(SOME_ERROR)
        setFlowEmission(state)

        /* Then */
        assertEmission(state)
        assertSnackbar(SnackbarMessage.Warning(SOME_ERROR))
    }

    private fun setFlowEmission(state: LatestReleaseState) {
        every { githubRepository.getLatestRelease() } returns flowOf(state)
    }

    private suspend fun assertEmission(state: LatestReleaseState) {
        useCase.getLatestRelease().test {
            /* Then */
            assertEquals(
                expected = state,
                actual = awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun assertSnackbar(message: SnackbarMessage?) {
        if (message == null) {
            coVerify(exactly = 0) { snackbarFeed.add(any()) }
        } else {
            coVerify { snackbarFeed.add(message) }
        }
        confirmVerified(snackbarFeed)
    }

    private companion object {
        const val NO_UPDATE = "no updates!"
        const val NO_RELEASES = "no releases!"
        const val SOME_ERROR = "some other error!"
    }
}
