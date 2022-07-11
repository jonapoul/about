package com.jonapoul.about.di

import java.io.File

/**
 * This interface should be implemented and provided via Hilt in any consuming applications.
 */
interface AboutResources {
    val logDirectory: File

    /* Should be a string resource ID */
    val appName: Int

    /* Should be a drawable or mipmap resource ID */
    val appIconResource: Int

    /* A string in the format "/repos/USERNAME/REPONAME/releases" */
    val githubReleasesUrl: String

    val githubIssuesUrl: String

    val githubUrl: String

    val developerName: String

    val developmentYear: Int

    val softwareLicense: String

    val logDescriptionText: String

    val showLogsButton: Boolean

    /* Pull all contents of the generated JSON file containing the third-party licenses */
    fun readLicensesJsonString(): String

    fun logZipFilename(timestamp: String): String
}
