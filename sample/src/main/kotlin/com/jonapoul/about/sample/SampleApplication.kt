package com.jonapoul.about.sample

import android.app.Application
import com.jonapoul.common.domain.init.AppInitialisers
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SampleApplication : Application() {
    @Inject
    lateinit var appInitialisers: AppInitialisers

    override fun onCreate() {
        super.onCreate()
        appInitialisers.init(app = this)
    }
}
