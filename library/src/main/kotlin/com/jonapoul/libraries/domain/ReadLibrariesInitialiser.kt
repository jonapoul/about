package com.jonapoul.libraries.domain

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.core.PrefPair
import com.jonapoul.common.domain.getInt
import com.jonapoul.common.domain.init.IAppInitialiser
import com.jonapoul.libraries.data.LicensesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * When the app launches:
 *      a) check whether we need to update the database with our app dependencies.
 *      b) if we do, parse the values and insert into the database asynchronously
 *      c) then update the preference value so we don't need to re-run this process every time.
 *
 * This means that the work of this class should only need to be re-run whenever the app updates.
 */
internal class ReadLibrariesInitialiser @Inject internal constructor(
    private val prefs: SharedPreferences,
    private val buildConfig: IBuildConfig,
    private val licensesRepository: LicensesRepository,
    private val scope: CoroutineScope,
) : IAppInitialiser {
    override fun init(app: Application) {
        if (!hasBeenInitialised()) {
            insertIntoDatabase()
            updatePreference()
        }
    }

    private fun hasBeenInitialised(): Boolean {
        val versionInitialised = prefs.getInt(APP_VERSION_LICENSES_INITIALISED)
        return versionInitialised == buildConfig.versionCode
    }

    private fun insertIntoDatabase() {
        scope.launch {
            licensesRepository.insertIntoDatabase()
        }
    }

    private fun updatePreference() {
        prefs.edit {
            putInt(APP_VERSION_LICENSES_INITIALISED.key, buildConfig.versionCode)
        }
    }

    private companion object {
        val APP_VERSION_LICENSES_INITIALISED = PrefPair(
            key = "licenses_initialised",
            default = 0
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface BindsReadLibrariesInitialiserModule {
    @Binds
    @IntoSet
    fun initialiser(bind: ReadLibrariesInitialiser): IAppInitialiser
}
