package com.jonapoul.libraries.di

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.jonapoul.common.core.ifTrue
import com.jonapoul.libraries.data.db.LibrariesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ProvidesDatabaseModule {
    @Singleton
    @Provides
    fun database(@ApplicationContext context: Context): LibrariesDatabase =
        Room.databaseBuilder(context, LibrariesDatabase::class.java, "libraries.db")
            .fallbackToDestructiveMigration()
            .ifTrue(Debug.isDebuggerConnected()) { allowMainThreadQueries() }
            .build()
}
