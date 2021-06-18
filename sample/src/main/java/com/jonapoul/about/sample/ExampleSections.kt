package com.jonapoul.about.sample

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jonapoul.about.AboutItem
import com.jonapoul.about.AboutSection

object ExampleSections {
    private const val GITHUB_ID = "jonapoul/about"
    private const val REDDIT_SUB = "/r/androiddev"

    val BASIC = listOf(
        AboutSection(
            title = R.string.section_header_1,
            items = listOf(
                AboutItem.fromVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                AboutItem.fromBuildType(BuildConfig.BUILD_TYPE),
                AboutItem.fromBuildTimeMs(BuildConfig.BUILD_TIME_MS),
            )
        ),
        AboutSection(
            title = R.string.section_header_2,
            items = listOf(
                AboutItem(
                    icon = R.drawable.ic_github,
                    title = "GitHub",
                    subtitle = GITHUB_ID,
                    onClickButton = {
                        openWebPage(context = it, url = "https://github.com/$GITHUB_ID")
                    }
                ),
                AboutItem(
                    icon = R.drawable.ic_reddit,
                    title = "Reddit",
                    subtitle = REDDIT_SUB,
                    onClickButton = {
                        openWebPage(context = it, url = "https://reddit.com$REDDIT_SUB")
                    }
                ),
                AboutItem.fromEmail("contact@company.org")
            )
        )
    )

    private fun openWebPage(context: Context, url: String) {
        /* Open a web browser to the page, or an app if installed */
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
