package com.jonapoul.libraries.di

import com.jonapoul.libraries.data.db.LibrariesDatabase
import com.jonapoul.libraries.data.db.LibraryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ProvidesLibraryDaoModule {
    @Provides
    fun provides(db: LibrariesDatabase): LibraryDao = db.libraryDao()
}
