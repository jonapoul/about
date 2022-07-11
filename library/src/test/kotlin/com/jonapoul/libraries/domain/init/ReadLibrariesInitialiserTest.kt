package com.jonapoul.libraries.domain.init

import android.app.Application
import android.content.SharedPreferences
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.test.CoroutineRule
import com.jonapoul.libraries.data.LicensesRepository
import com.jonapoul.libraries.domain.ReadLibrariesInitialiser
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class ReadLibrariesInitialiserTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineRule = CoroutineRule()

    @MockK
    private lateinit var app: Application

    @RelaxedMockK
    private lateinit var prefs: SharedPreferences

    @RelaxedMockK
    private lateinit var editor: SharedPreferences.Editor

    @MockK
    private lateinit var buildConfig: IBuildConfig

    @RelaxedMockK
    private lateinit var repo: LicensesRepository

    private lateinit var initialiser: ReadLibrariesInitialiser

    private val scope: CoroutineScope
        get() = coroutineRule.scope

    @Before
    fun before() {
        every { prefs.edit() } returns editor
        every { prefs.getInt(any(), any()) } returns APP_VERSION
        initialiser = ReadLibrariesInitialiser(prefs, buildConfig, repo, scope)
    }

    @Test
    fun `Uninitialised, data is inserted and version number is saved`() = runTest {
        /* Given */
        every { prefs.getInt(any(), any()) } returns 0 // nothing saved
        every { buildConfig.versionCode } returns APP_VERSION

        /* When */
        initialiser.init(app)
        coroutineRule.advanceUntilIdle()

        /* Then */
        coVerify(exactly = 1) { repo.insertIntoDatabase() }
        verify(exactly = 1) { editor.putInt(any(), APP_VERSION) }
    }

    @Test
    fun `Already initialised, nothing happens`() = runTest {
        /* Given */
        every { buildConfig.versionCode } returns APP_VERSION

        /* When */
        initialiser.init(app)
        coroutineRule.advanceUntilIdle()

        /* Then */
        coVerify(exactly = 0) { repo.insertIntoDatabase() }
        verify(exactly = 0) { editor.putInt(any(), any()) }
    }

    @Test
    fun `Increased version code, update database`() = runTest {
        /* Given */
        every { buildConfig.versionCode } returns APP_VERSION + 1

        /* When */
        initialiser.init(app)
        coroutineRule.advanceUntilIdle()

        /* Then */
        coVerify(exactly = 1) { repo.insertIntoDatabase() }
        verify(exactly = 1) { editor.putInt(any(), APP_VERSION + 1) }
    }

    @Test
    fun `Decreased version code, still update database`() = runTest {
        /* Given */
        every { buildConfig.versionCode } returns APP_VERSION - 1

        /* When */
        initialiser.init(app)
        coroutineRule.advanceUntilIdle()

        /* Then */
        coVerify(exactly = 1) { repo.insertIntoDatabase() }
        verify(exactly = 1) { editor.putInt(any(), APP_VERSION - 1) }
    }

    private companion object {
        const val APP_VERSION = 69
    }
}
