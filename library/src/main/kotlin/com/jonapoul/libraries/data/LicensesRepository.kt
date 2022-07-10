package com.jonapoul.libraries.data

import com.jonapoul.about.di.AboutResources
import com.jonapoul.common.core.IBuildConfig
import com.jonapoul.common.di.IODispatcher
import com.jonapoul.libraries.data.db.LibraryDao
import com.jonapoul.libraries.data.db.LibraryEntity
import com.jonapoul.libraries.data.json.LibraryModel
import com.jonapoul.libraries.domain.LibrariesTextCreator
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LicensesRepository @Inject constructor(
    moshi: Moshi,
    librariesTextCreator: LibrariesTextCreator,
    @IODispatcher private val io: CoroutineDispatcher,
    private val aboutResources: AboutResources,
    private val buildConfig: IBuildConfig,
    private val libraryDao: LibraryDao,
) {
    private val unknown = librariesTextCreator.unknown

    private val libraryListAdapter = moshi.adapter<List<LibraryModel>>(
        Types.newParameterizedType(List::class.java, LibraryModel::class.java)
    )

    suspend fun getOpenSourceLicenses(): List<LibraryEntity> =
        withContext(io) { libraryDao.getAllLibraries() }

    suspend fun insertIntoDatabase() {
        withContext(io) {
            /* Delete any from the database from a previous version of the app */
            libraryDao.deleteAllWithDifferentVersionCode(buildConfig.versionCode)

            /* Parse the JSON file from app assets */
            val entities = readModelsFromJson().map { it.toEntity() }

            /* Insert them into the database */
            libraryDao.insertAll(entities)
        }
    }

    private fun readModelsFromJson(): List<LibraryModel> {
        /* This JSON file is generated at build time by the "gradle-license-plugin" plugin
         * and placed in the assets directory of the "app" module */
        val jsonString = aboutResources.readLicensesJsonString()
        val list = libraryListAdapter.fromJson(jsonString) ?: emptyList()
        return list
            .sortedBy { it.project }
            .onEach { it.cleanUpLicenses() }
    }

    private fun LibraryModel.toEntity(): LibraryEntity = LibraryEntity(
        appVersionCode = buildConfig.versionCode,
        project = project,
        version = version,
        artifact = getArtifact(),
        authors = getAuthors(),
        description = description ?: unknown,
        url = licenses.getOrNull(0)?.licenseUrl,
        license = licenses.getOrNull(0)?.license ?: unknown
    )

    private fun LibraryModel.getAuthors(): String {
        val authors = developers.filter { it.isNotBlank() }
        return if (authors.isNotEmpty()) {
            /* Convert ["Alice", "Bob", "Charlie"] to "Alice, Bob, Charlie" */
            authors.joinToString(", ")
        } else {
            unknown
        }
    }

    private fun LibraryModel.getArtifact(): String {
        /* Convert "androidx.activity:activity:1.3.1" to "androidx.activity:activity" */
        return dependency
            .split(":")
            .subList(fromIndex = 0, toIndex = 2)
            .joinToString(separator = ":")
    }
}
