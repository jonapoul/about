package com.jonapoul.about.data

import retrofit2.http.GET
import retrofit2.http.Url

internal interface GithubApi {
    @GET
    suspend fun getAppReleases(@Url url: String): List<GithubReleaseModel>
}
