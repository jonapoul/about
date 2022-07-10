package com.jonapoul.libraries.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [LibraryEntity::class],
)
internal abstract class LibrariesDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
}
