package com.jonapoul.libraries.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface LibraryDao {
    @Insert
    suspend fun insertAll(entities: List<LibraryEntity>)

    @Query("SELECT * FROM Libraries")
    suspend fun getAllLibraries(): List<LibraryEntity>

    @Query("DELETE FROM Libraries WHERE appVersionCode <> :versionCode")
    suspend fun deleteAllWithDifferentVersionCode(versionCode: Int)
}
