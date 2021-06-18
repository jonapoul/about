package com.jonapoul.about

import androidx.annotation.DrawableRes
import com.jonapoul.extensions.capitalized
import java.text.SimpleDateFormat
import java.util.*

data class AboutItem(
    @DrawableRes val icon: Int,
    val title: String,
    val subtitle: String,
    val onClickButton: OnClickListener? = null,
) {

    /**
     * Some items that I commonly use in my own projects.
     */
    companion object {
        /**
         * Use as:  `AboutItem.fromVersionName(BuildConfig.VERSION_NAME)`
         */
        fun fromVersion(versionName: String, versionCode: Int): AboutItem = AboutItem(
            icon = R.drawable.ic_info,
            title = "Version",
            subtitle = "$versionName ($versionCode)"
        )

        /**
         * Use as:  `AboutItem.fromBuildType(BuildConfig.BUILD_TYPE)`
         */
        fun fromBuildType(buildType: String): AboutItem = AboutItem(
            icon = R.drawable.ic_build,
            title = "Build Type",
            subtitle = buildType.capitalized()
        )

        /**
         * For this one you'll need to add an extra field in your app-level build.gradle file:
         *      android {
         *          ...
         *          defaultConfig {
         *              ...
         *              buildConfigField "long", "BUILD_TIME_MS", System.currentTimeMillis() + "L"
         *          }
         *      }
         * Then use as:  `AboutItem.fromBuildTime(BuildConfig.BUILD_TIME_MS)`
         */
        fun fromBuildTimeMs(buildTimeMs: Long): AboutItem = fromBuildDate(Date(buildTimeMs))

        fun fromBuildDate(buildDate: Date): AboutItem = AboutItem(
            icon = R.drawable.ic_date,
            title = "Build Time",
            subtitle = SimpleDateFormat("HH:mm:ss dd MMM yyyy z", Locale.getDefault())
                .format(buildDate)
        )
    }
}
