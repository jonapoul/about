package com.jonapoul.about.sample

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jonapoul.common.di.IODispatcher
import com.jonapoul.common.domain.init.IAppInitialiser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.threeten.bp.zone.ZoneRulesProvider
import javax.inject.Inject

internal class ThreeTenInitialiser @Inject constructor(
    @IODispatcher private val io: CoroutineDispatcher,
    private val scope: CoroutineScope,
) : IAppInitialiser {
    override fun init(app: Application) {
        /* Initialise ThreeTenABP */
        AndroidThreeTen.init(app)

        /* Query the ZoneRulesProvider so that it is loaded on a background coroutine */
        scope.launch(io) {
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface BindsThreeTenInitialiserModule {
    @Binds
    @IntoSet
    fun initialiser(bind: ThreeTenInitialiser): IAppInitialiser
}
