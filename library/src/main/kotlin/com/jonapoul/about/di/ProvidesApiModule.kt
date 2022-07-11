package com.jonapoul.about.di

import com.jonapoul.about.data.GithubApiBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ProvidesApiModule {
    @Provides
    fun providesGithubApi(
        builder: GithubApiBuilder,
    ) = builder.buildApi()
}
