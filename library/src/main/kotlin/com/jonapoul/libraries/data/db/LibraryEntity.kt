package com.jonapoul.libraries.data.db

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "Libraries")
internal data class LibraryEntity(
    @ColumnInfo
    val appVersionCode: Int,

    @ColumnInfo
    val project: String,

    @ColumnInfo
    val description: String?,

    @ColumnInfo
    val version: String,

    @ColumnInfo
    val authors: String?,

    @ColumnInfo
    val url: String?,

    @ColumnInfo
    val license: String?,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    val artifact: String,
)
