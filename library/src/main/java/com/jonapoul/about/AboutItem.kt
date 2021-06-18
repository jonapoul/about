package com.jonapoul.about

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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

    fun interface OnClickListener {
        fun onItemClick(context: Context)
    }

    companion object {

        @SuppressLint("QueryPermissionsNeeded")
        fun fromEmail(emailAddress: String): AboutItem = AboutItem(
            icon = R.drawable.ic_email,
            title = "Email",
            subtitle = emailAddress,
            onClickButton = { ctx ->
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                try {
                    ctx.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(ctx, "No email clients found!", Toast.LENGTH_LONG).show()
                }
            }
        )

        fun fromVersion(versionName: String, versionCode: Int): AboutItem = AboutItem(
            icon = R.drawable.ic_info,
            title = "Version",
            subtitle = "$versionName ($versionCode)"
        )

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
