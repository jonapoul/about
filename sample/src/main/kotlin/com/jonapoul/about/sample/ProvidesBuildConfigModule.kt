package com.jonapoul.about.sample

import com.jonapoul.common.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProvidesBuildConfigModule {
    @Provides
    fun provides(): IBuildConfig = object : IBuildConfig {
        override val debug = BuildConfig.DEBUG
        override val versionName = BuildConfig.VERSION_NAME
        override val versionCode = BuildConfig.VERSION_CODE
        override val gitId = BuildConfig.GIT_ID
        override val buildTime = BuildConfig.BUILD_TIME
    }
}
