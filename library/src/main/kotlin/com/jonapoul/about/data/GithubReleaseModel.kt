package com.jonapoul.about.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
internal data class GithubReleaseModel(
    /* The title (version string) of the release. E.g. "1.2.3" */
    @Json(name = "name")
    val versionName: String,

    /* The ISO-8601 string timestamp of the publish. E.g. "2021-11-06T12:15:10Z" */
    @Json(name = "published_at")
    val publishedAt: String,

    /* The URL of the release tag. E.g. "https://github.com/jonapoul/about/releases/tag/1.0.0" */
    @Json(name = "html_url")
    val htmlUrl: String,
)
