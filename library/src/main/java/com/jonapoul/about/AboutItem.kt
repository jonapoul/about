package com.jonapoul.about

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jonapoul.extensions.string.capitalized
import java.text.SimpleDateFormat
import java.util.*

/**
 * An object representing a single row to be displayed in either an [AboutDialogBuilder] or a
 * [AboutFragment].
 * @param icon The Drawable resource ID of an icon to be displayed on the left hand side.
 * @param title The string to be displayed as the row's title.
 * @param subtitle The string to be displayed as the row's subtitle/value.
 * @param onClick A nullable callback to be invoked when the user taps on the row. If not null,
 *                      a "launch" button will be displayed on the right hand side to make the row's
 *                      clickability more obvious.
 */
data class AboutItem(
    @DrawableRes val icon: Int,
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val subtitle: String? = null,
    @StringRes val subtitleRes: Int? = null,
    val onClick: OnClickListener? = null
) {

    /**
     * A functional interface to be called when an [AboutItem] is clicked by the user.
     */
    fun interface OnClickListener {
        /**
         * Called when the [AboutItem] is clicked by the user.
         */
        fun onItemClick(context: Context)
    }

    /**
     * Companion object
     */
    companion object {

        /**
         * Constructs an [AboutItem] with an email icon and the title "Email". Tapping this item
         * will launch an installed email client addressed to the [emailAddress] parameter.
         * @param emailAddress The email address to which the user should be directed on click
         */
        @SuppressLint("QueryPermissionsNeeded")
        fun fromEmail(emailAddress: String): AboutItem = AboutItem(
            icon = R.drawable.ic_email,
            titleRes = R.string.about_email,
            subtitle = emailAddress,
            onClick = { ctx ->
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                try {
                    ctx.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(ctx, R.string.about_email_no_clients, Toast.LENGTH_LONG).show()
                }
            }
        )

        /**
         * Constructs an [AboutItem] with an "info" icon and the title "Version". It will format
         * the passed parameters as "$versionName ($versionCode)" and place them in the item's
         * subtitle.
         * @param versionName This should be your app's `BuildConfig.VERSION_NAME`
         * @param versionCode This should be your app's `BuildConfig.VERSION_CODE`
         */
        fun fromVersion(versionName: String, versionCode: Int): AboutItem = AboutItem(
            icon = R.drawable.ic_info,
            titleRes = R.string.about_version,
            subtitle = "$versionName ($versionCode)"
        )

        /**
         * Constructs an [AboutItem] with a "build" icon and the title "Build Type". It will format
         * the passed parameters as "$versionName ($versionCode)" and place them in the item's
         * subtitle.
         * @param buildType This should be your app's `BuildConfig.BUILD_TYPE`
         */
        fun fromBuildType(buildType: String): AboutItem = AboutItem(
            icon = R.drawable.ic_build,
            titleRes = R.string.about_build_type,
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
         * @param buildTimeMs The unix timestamp (in milliseconds) of the app's compilation.
         */
        fun fromBuildTimeMs(buildTimeMs: Long): AboutItem = AboutItem(
            icon = R.drawable.ic_date,
            titleRes = R.string.about_build_time,
            subtitle = SimpleDateFormat("HH:mm:ss dd MMM yyyy z", Locale.getDefault())
                .format(Date(buildTimeMs))
        )

        /**
         * Constructs an [AboutItem] with the supplied icon, title and URL as a subtitle. When
         * clicked by the user, the supplied [url] will be launched in the phone's browser.
         * @param icon The icon displayed to the left of the row
         * @param name The title of the website
         * @param url The fully-qualified URL of the website
         */
        fun fromWebsite(
            @DrawableRes icon: Int,
            @StringRes name: Int,
            url: String
        ): AboutItem = AboutItem(
            icon = icon,
            titleRes = name,
            subtitle = url,
            onClick = { openWebPage(it, url) }
        )

        /**
         * Constructs an [AboutItem] with the GitHub logo, "GitHub" title and the supplied URL as a
         * subtitle. When clicked by the user, the supplied [url] will be launched in the phone's
         * browser. This URL does not necessarily need to be in the github.com domain, but
         * obviously it makes sense to do that.
         * @param url The fully-qualified URL of the web page
         */
        fun fromGithub(url: String): AboutItem = fromWebsite(
            icon = R.drawable.ic_github,
            name = R.string.about_github,
            url = url
        )

        /**
         * Constructs an [AboutItem] with the Discord logo, "Discord" title and the supplied URL as a
         * subtitle. When clicked by the user, the supplied [url] will be launched in the phone's
         * browser. This URL does not necessarily need to be in the discord.com/gg domain, but
         * obviously it makes sense to do that.
         * @param url The fully-qualified URL of the web page
         */
        fun fromDiscord(url: String): AboutItem = fromWebsite(
            icon = R.drawable.ic_discord,
            name = R.string.about_discord,
            url = url
        )

        /**
         * Constructs an [AboutItem] with the Reddit logo, "Reddit" title and the supplied URL as a
         * subtitle. When clicked by the user, the supplied [url] will be launched in the phone's
         * browser. This URL does not necessarily need to be in the reddit.com domain, but
         * obviously it makes sense to do that.
         * @param url The fully-qualified URL of the web page
         */
        fun fromReddit(url: String): AboutItem = fromWebsite(
            icon = R.drawable.ic_reddit,
            name = R.string.about_reddit,
            url = url
        )

        /**
         * Launches the given [url] web page in the device's web browser.
         */
        private fun openWebPage(context: Context, url: String) {
            /* Open a web browser to the page, or an app if installed */
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
