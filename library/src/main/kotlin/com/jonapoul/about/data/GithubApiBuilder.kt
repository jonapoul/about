package com.jonapoul.about.data

import com.jonapoul.common.data.api.ApiBuilder
import com.jonapoul.common.data.api.OkHttpClientFactory
import retrofit2.Retrofit
import javax.inject.Inject

internal class GithubApiBuilder @Inject constructor(
    retrofitBuilder: Retrofit.Builder,
    okHttpClientFactory: OkHttpClientFactory,
) : ApiBuilder<GithubApi>(
    retrofitBuilder,
    okHttpClientFactory,
    apiClass = GithubApi::class.java,
    baseUrl = "api.github.com",
)
