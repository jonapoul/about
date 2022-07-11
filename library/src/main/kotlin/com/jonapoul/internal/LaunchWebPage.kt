package com.jonapoul.internal

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

internal suspend fun launchWebPage(context: Context, url: String, onActivityNotFound: suspend () -> Unit) {
    /* Open a web browser to the page, or another app if one is installed */
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    try {
        ContextCompat.startActivity(context, intent, null)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFound.invoke()
    }
}
