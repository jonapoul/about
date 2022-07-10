# About

![Build](https://github.com/jonapoul/about/actions/workflows/actions.yml/badge.svg)
[![Jitpack](https://jitpack.io/v/jonapoul/about.svg)](https://jitpack.io/#jonapoul/about)

## Summary
An Android library which gives you a set of three screens:
- About the app
- All supporting third-party libraries
- Export debugging logs (optional)

## Screenshots
TBC

## Setup

### Library Import
Root-level `build.gradle`:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Module-level `build.gradle`:
```gradle
dependencies {
    implementation "com.github.jonapoul:about:{version}"
}
```

### Other Dependencies/Setup
This library also requires the following frameworks, since I only really wrote it for my own uses:
- [Dagger Hilt](https://dagger.dev/hilt/)
    - An implementation of the [IBuildConfig](https://github.com/jonapoul/android-common/blob/master/lib-core/src/main/kotlin/com/jonapoul/common/core/IBuildConfig.kt) interface from my [android-common](https://github.com/jonapoul/android-common) library, bound via Hilt
    - An implementation of the [AboutResources](https://github.com/jonapoul/about/blob/master/library/src/main/kotlin/com/jonapoul/about/di/AboutResources.kt) interface from this library, bound via Hilt
- [AndroidX Navigation](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Android ThreeTenBP](https://github.com/JakeWharton/ThreeTenABP)
- [Gradle License Plugin](https://github.com/jaredsburrows/gradle-license-plugin)
- Probably others(?)

For the Gradle License plugin, first import the plugin to your classpath (in the root build file):
```groovy
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "com.jaredsburrows:gradle-license-plugin:0.9.0"
    }
}
```

Then add to your module-level build file as:
```groovy
apply plugin: "com.jaredsburrows.license"

android {
    ...
}

licenseReport {
    /* We only want JSON data to be put in the app assets, to read at runtime */
    generateJsonReport = true
    copyJsonReportToAssets = true

    /* Don't generate CSV/HTML output */
    generateCsvReport = false
    generateHtmlReport = false
    copyCsvReportToAssets = false
    copyHtmlReportToAssets = false
}

afterEvaluate {
    /* Make sure the licenses JSON is generated whenever we build the app */
    preBuild.dependsOn licenseReleaseReport
    assembleDebug.dependsOn licenseReleaseReport
    assembleRelease.dependsOn licenseReleaseReport

    /* Delete the existing JSON file before we regenerate it. If we don't do this, the plugin
     * doesn't overwrite the existing one so any dependency changes won't be reflected */
    tasks.licenseReleaseReport.doFirst {
        def file = new File("${project.projectDir}/src/main/assets/open_source_licenses.json")
        file.delete()
    }
}
```

This will spit out a JSON file into your module's assets folder, so I'd recommend adding the following to your .gitignore file:

```
open_source_licenses.json
```

In future I'll probably find a way of incorporating all this setup into the library itself, but that's a task for another day.

## Usage

### Navigation

In your app's navigation graph XML resource file, add the following:
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@+id/nav_home">

    <fragment
        android:id="@+id/nav_home"
        android:name="com.jonapoul.about.sample.HomeFragment"
        android:label="@string/app_name"
        tools:layout="@layout/fragment_home">

        <action
            android:id="@+id/to_about"
            app:destination="@id/nav_graph_about" />

    </fragment>

    <include app:graph="@navigation/nav_graph_about" />

</navigation>
```

Make sure that the `app:destination` in the navigation action block exactly matches `@id/nav_graph_about`, not `nav_graph`!

Now you can just use a `NavController` instance to show the about screen.

### Implementing Classes
Two interfaces need to be implemented and bound via Hilt. Examples:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
class ProvidesBuildConfigModule {
    @Provides
    @Singleton
    fun buildConfig(): IBuildConfig = object : IBuildConfig {
        override val debug = BuildConfig.DEBUG
        override val versionName = BuildConfig.VERSION_NAME
        override val versionCode = BuildConfig.VERSION_CODE
        override val gitId = BuildConfig.GIT_ID
        override val buildTime = BuildConfig.BUILD_TIME
    }

    @Provides
    @Singleton
    fun aboutResources(@ApplicationContext context: Context): AboutResources = object : AboutResources {
        override val logDirectory: File = File(context.dataDir, "logs")
        override val appName: Int = R.string.app_name
        override val appIconResource: Int = R.mipmap.ic_launcher_round
        override val githubReleasesUrl: String = "/repos/jonapoul/about/releases"
        override val githubIssuesUrl: String = "https://github.com/jonapoul/about/issues/new"
        override val githubUrl: String = "https://github.com/jonapoul/about"
        override val developerName: String = "Jon Poulton"
        override val developmentYear: Int = 2022
        override val softwareLicense: String = "Apache 2.0"
        override val logDescriptionText: String = context.getString(R.string.log_description_text)
        override val showLogsButton: Boolean = false
        override fun logZipFilename(timestamp: String): String = "about_logs_$timestamp.zip"

        override fun readLicensesJsonString(): String =
            context.assets
                .open(LICENSES_ASSET_FILENAME)
                .reader()
                .use { it.readText() }
    }

    private companion object {
        const val LICENSES_ASSET_FILENAME = "open_source_licenses.json"
    }
}
```
