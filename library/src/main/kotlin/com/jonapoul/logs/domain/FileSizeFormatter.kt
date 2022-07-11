package com.jonapoul.logs.domain

import androidx.annotation.IntRange
import java.util.Locale
import javax.inject.Inject

internal class FileSizeFormatter @Inject constructor() {
    fun bytesToString(bytes: Long, @IntRange(from = 0) decimalPlaces: Int): String =
        when {
            bytes < KB -> "$bytes B"
            bytes < MB -> toKilobytes(bytes, decimalPlaces)
            bytes < GB -> toMegabytes(bytes, decimalPlaces)
            bytes < TB -> toGigabytes(bytes, decimalPlaces)
            else -> toTerabytes(bytes, decimalPlaces)
        }

    private fun toKilobytes(bytes: Long, decimalPlaces: Int): String =
        String.format(locale, "%.${decimalPlaces}f kB", bytes.toFloat() / KB)

    private fun toMegabytes(bytes: Long, decimalPlaces: Int): String =
        String.format(locale, "%.${decimalPlaces}f MB", bytes.toFloat() / MB)

    private fun toGigabytes(bytes: Long, decimalPlaces: Int): String =
        String.format(locale, "%.${decimalPlaces}f GB", bytes.toFloat() / GB)

    private fun toTerabytes(bytes: Long, decimalPlaces: Int): String =
        String.format(locale, "%.${decimalPlaces}f TB", bytes.toFloat() / TB)

    private val locale: Locale
        get() = Locale.getDefault()

    private companion object {
        const val KB = 1000.0f
        const val MB = 1000.0f * KB
        const val GB = 1000.0f * MB
        const val TB = 1000.0f * GB
    }
}
