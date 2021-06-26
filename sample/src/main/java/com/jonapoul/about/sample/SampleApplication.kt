package com.jonapoul.about.sample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.jonapoul.extensions.sharedprefs.getIntFromPair

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        AppCompatDelegate.setDefaultNightMode(
            prefs.getIntFromPair(Prefs.APP_THEME)
        )
    }
}
