package com.jonapoul.libraries.data.json

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
internal data class LicenseModel(
    @Json(name = "license")
    var license: String,

    @Json(name = "license_url")
    val licenseUrl: String,
) {
    fun cleanUpLicense() {
        LICENSES_MAP.forEach { (regex, shortened) ->
            if (license.contains(regex)) {
                license = shortened
            }
        }
    }

    private companion object {
        val LICENSES_MAP = mapOf(
            "Apache.*?2\\.0".toRegex() to "Apache 2.0",
            "MIT".toRegex() to "MIT",
        )
    }
}
