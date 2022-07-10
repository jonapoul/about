package com.jonapoul.logs.domain

import android.content.Context
import com.jonapoul.about.R
import com.jonapoul.common.core.requireMessage
import com.jonapoul.common.domain.TextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.Duration
import javax.inject.Inject

internal class ExportLogsTextCreator @Inject constructor(
    @ApplicationContext context: Context,
) : TextCreator(context) {

    val succeededExporting: String =
        fromRes(R.string.export_file_succeeded)

    fun failedExporting(exception: Exception): String =
        context.getString(R.string.export_file_failed, exception.requireMessage())

    fun deleted(numFiles: Int): String =
        context.resources.getQuantityString(R.plurals.logs_delete_completed, numFiles, numFiles)

    fun formatDuration(duration: Duration): String {
        val days = duration.toDays().toInt()
        val hours = duration.toHours().toInt()
        val minutes = duration.toMinutes().toInt()
        val res = context.resources
        return when {
            days > 0 -> res.getQuantityString(R.plurals.logging_since_days, days, days)
            hours > 0 -> res.getQuantityString(R.plurals.logging_since_hours, hours, hours)
            minutes > 0 -> res.getQuantityString(R.plurals.logging_since_minutes, minutes, minutes)
            else -> res.getQuantityString(R.plurals.logging_since_minutes, 0, 0)
        }
    }
}
