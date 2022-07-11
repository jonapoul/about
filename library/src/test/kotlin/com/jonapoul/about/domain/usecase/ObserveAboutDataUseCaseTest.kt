package com.jonapoul.about.domain.usecase

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import app.cash.turbine.test
import com.jonapoul.about.data.AboutState
import com.jonapoul.about.di.AboutResources
import com.jonapoul.about.domain.AboutTextCreator
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.test.CoroutineRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import java.util.Locale
import kotlin.test.assertEquals

internal class ObserveAboutDataUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val coroutineRule = CoroutineRule()

    @MockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var buildConfig: IBuildConfig

    @RelaxedMockK
    private lateinit var textCreator: AboutTextCreator

    @RelaxedMockK
    private lateinit var aboutResources: AboutResources

    @MockK
    private lateinit var packageManager: PackageManager

    private lateinit var useCase: ObserveAboutDataUseCase

    @Before
    fun before() {
        mockkStatic(ZoneId::class)
        every { ZoneId.systemDefault() } returns ZONE_UTC
        every { context.packageName } returns PACKAGE_NAME
        every { context.packageManager } returns packageManager
        every { packageManager.getPackageInfo(ofType<String>(), any()) } returns PackageInfo()
            .also { it.firstInstallTime = INSTALL_TIME.toEpochMilli() }

        useCase = ObserveAboutDataUseCase(
            context,
            textCreator,
            aboutResources,
            buildConfig
        )
    }

    @Test
    fun `Flow returns correct values`() = runTest {
        /* Given */
        val version = "1.2.3 (4)"
        val gitId = "ABCD1234"
        val appName = 1
        val appIconResource = 2
        val showLogsButton = true
        val developedBy = "Dick Tracy, 2022"
        val softwareLicense = "Whatever License"

        every { buildConfig.buildTime } returns BUILD_TIME
        every { textCreator.version } returns version
        every { buildConfig.gitId } returns gitId
        every { aboutResources.appName } returns appName
        every { aboutResources.appIconResource } returns appIconResource
        every { aboutResources.showLogsButton } returns showLogsButton
        every { textCreator.developedBy } returns developedBy
        every { aboutResources.softwareLicense } returns softwareLicense

        /* When */
        useCase.buildState.test {
            /* Then */
            assertEquals(
                actual = awaitItem(),
                expected = AboutState(
                    versionInfo = version,
                    gitId = gitId,
                    buildTimestamp = "12:34:56, Sun 02 Jan 2022 +0000",
                    installTimestamp = "12:34:56, Sat 02 Jul 2022 +0000",
                    appName = appName,
                    developedBy = developedBy,
                    softwareLicense = softwareLicense,
                    appIconResource = appIconResource,
                    showLogsButton = showLogsButton,
                ),
            )
            awaitComplete()
        }
    }

    companion object {
        private const val PACKAGE_NAME = "com.package.name"
        private val ZONE_UTC = ZoneId.of("UTC")
        private lateinit var defaultLocale: Locale
        private val BUILD_TIME = Instant.parse("2022-01-02T12:34:56.789Z")
        private val INSTALL_TIME = Instant.parse("2022-07-02T12:34:56.789Z") // 6 months later

        @BeforeClass
        fun beforeClass() {
            defaultLocale = Locale.getDefault()
            Locale.setDefault(Locale.UK)
        }

        @AfterClass
        fun afterClass() {
            Locale.setDefault(defaultLocale)
        }
    }
}
