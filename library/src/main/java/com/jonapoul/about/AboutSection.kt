package com.jonapoul.about

import androidx.annotation.StringRes

data class AboutSection(
    val items: List<AboutItem>,
    @StringRes val title: Int? = null,
)
