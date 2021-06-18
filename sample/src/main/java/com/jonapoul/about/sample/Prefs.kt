package com.jonapoul.about.sample

import androidx.appcompat.app.AppCompatDelegate
import com.jonapoul.extensions.PrefPair

object Prefs {
    val APP_THEME = PrefPair("app_theme", AppCompatDelegate.MODE_NIGHT_NO)
}
