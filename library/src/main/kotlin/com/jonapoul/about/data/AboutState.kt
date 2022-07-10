package com.jonapoul.about.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class AboutState(
    val versionInfo: String,
    val gitId: String,
    val buildTimestamp: String,
    val installTimestamp: String,
    @StringRes val appName: Int,
    val developedBy: String,
    val softwareLicense: String,
    @DrawableRes val appIconResource: Int,
    val showLogsButton: Boolean,
)
