package com.jonapoul.libraries.data.json

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
internal data class LibraryModel(
    @Json(name = "project")
    val project: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "version")
    val version: String,

    @Json(name = "developers")
    val developers: MutableList<String>,

    @Json(name = "url")
    val url: String?,

    @Json(name = "licenses")
    val licenses: List<LicenseModel>,

    @Json(name = "dependency")
    val dependency: String,
) {
    fun cleanUpLicenses() {
        licenses.forEach { it.cleanUpLicense() }
    }
}
