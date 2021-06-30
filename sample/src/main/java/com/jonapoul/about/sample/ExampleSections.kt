package com.jonapoul.about.sample

import com.jonapoul.about.AboutItem
import com.jonapoul.about.AboutSection

object ExampleSections {
    val BASIC = listOf(
        AboutSection.fromBuildConfig(
            buildConfig = BuildConfig::class.java,
            buildTimeMsField = "BUILD_TIME_MS"
        ),
        AboutSection(
            title = R.string.section_header_2,
            items = listOf(
                AboutItem.fromGithub("https://github.com/jonapoul/about"),
                AboutItem.fromReddit("https://reddit.com/r/androiddev"),
                AboutItem.fromEmail("contact@company.org")
            )
        )
    )
}
