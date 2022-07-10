package com.jonapoul.logs.data

import com.jonapoul.common.data.localised
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject

internal class LogTimestampParser @Inject constructor() {
    fun parseTimestampOrNull(line: String): Instant? = try {
        /* Converts "2022-03-12 10:31:19.622 [main] DEBUG start" to "2022-03-12 10:31:19.622" */
        val timestamp = line.split(SPACE).subList(0, 2).joinToString(SPACE)
        val localDateTime = LocalDateTime.parse(timestamp, FORMATTER)
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
        zonedDateTime.toInstant()
    } catch (e: Exception) {
        Timber.w(e, "Failed parsing timestamp from line:\n$line")
        null
    }

    private companion object {
        const val SPACE = " "
        val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .localised()
    }
}
