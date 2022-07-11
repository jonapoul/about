package com.jonapoul.about.data

import app.cash.turbine.test
import com.jonapoul.about.di.AboutResources
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.test.CoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Instant
import kotlin.test.assertEquals
import kotlin.test.fail

internal class GithubRepositoryTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineRule = CoroutineRule()

    @MockK
    private lateinit var githubApi: GithubApi

    @MockK
    private lateinit var buildConfig: IBuildConfig

    @MockK
    private lateinit var aboutResources: AboutResources

    private lateinit var githubRepository: GithubRepository

    @Before
    fun before() {
        every { buildConfig.buildTime } returns APP_BUILD_TIME
        coEvery { githubApi.getAppReleases(any()) } returns THREE_RELEASES
        every { aboutResources.githubReleasesUrl } returns "www.url.com/releases" // irrelevant

        githubRepository = GithubRepository(
            io = coroutineRule.dispatcher,
            githubApi,
            aboutResources,
            buildConfig,
        )
    }

    @Test
    fun `No new update when app versions are the same`() = runTest {
        /* Given */
        every { buildConfig.versionName } returns NEWEST_RELEASE

        /* When */
        githubRepository.getLatestRelease().test {
            /* Then */
            assertEquals(expected = LatestReleaseState.Loading, actual = awaitItem())
            assertEquals(expected = LatestReleaseState.NoUpdate, actual = awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Update available`() = runTest {
        /* Given */
        every { buildConfig.versionName } returns OLDEST_RELEASE
        coEvery { githubApi.getAppReleases(any()) } returns THREE_RELEASES

        /* When */
        val latestRelease = THREE_RELEASES.maxByOrNull { it.publishedAt } ?: fail()
        githubRepository.getLatestRelease().test {
            /* Then */
            assertEquals(expected = LatestReleaseState.Loading, actual = awaitItem())
            assertEquals(expected = LatestReleaseState.UpdateAvailable(latestRelease), actual = awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `No releases`() = runTest {
        /* Given */
        every { buildConfig.versionName } returns OLDEST_RELEASE
        coEvery { githubApi.getAppReleases(any()) } returns emptyList()

        /* When */
        githubRepository.getLatestRelease().test {
            /* Then */
            assertEquals(expected = LatestReleaseState.Loading, actual = awaitItem())
            assertEquals(expected = LatestReleaseState.NoReleases, actual = awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Failure on Exception`() = runTest {
        /* Given */
        val message = "this message is caught and handled nicely"
        coEvery { githubApi.getAppReleases(any()) } throws Exception(message)

        /* When */
        githubRepository.getLatestRelease().test {
            /* Then */
            assertEquals(expected = LatestReleaseState.Loading, actual = awaitItem())
            assertEquals(expected = LatestReleaseState.Failure(message), actual = awaitItem())
            awaitComplete()
        }
    }

    private companion object {
        const val OLDEST_RELEASE = "1.0.0"
        const val MIDDLE_RELEASE = "1.1.0"
        const val NEWEST_RELEASE = "2.0.0"
        val APP_BUILD_TIME = Instant.ofEpochMilli(1636297142409L)!!

        val THREE_RELEASES = listOf(
            GithubReleaseModel(
                versionName = OLDEST_RELEASE,
                publishedAt = "2021-11-06T12:15:10Z",
                htmlUrl = "http://www.example.com/1.0.0"
            ),
            GithubReleaseModel(
                versionName = MIDDLE_RELEASE,
                publishedAt = "2021-11-07T12:15:10Z",
                htmlUrl = "http://www.example.com/1.1.0"
            ),
            GithubReleaseModel(
                versionName = NEWEST_RELEASE,
                publishedAt = "2021-11-08T12:15:10Z",
                htmlUrl = "http://www.example.com/2.0.0"
            )
        )
    }
}
