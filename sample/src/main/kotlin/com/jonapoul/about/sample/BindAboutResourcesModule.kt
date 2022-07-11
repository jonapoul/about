package com.jonapoul.about.sample

import com.jonapoul.about.di.AboutResources
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindAboutResourcesModule {
    @Binds
    fun resources(
        aboutResources: SampleAboutResources,
    ): AboutResources
}
