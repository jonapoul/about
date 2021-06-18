# about

![Build](https://github.com/jonapoul/about/actions/workflows/build.yml/badge.svg)
[![Jitpack](https://jitpack.io/v/jonapoul/about.svg)](https://jitpack.io/#jonapoul/about)

## Summary
A collection of two useful views/fragments that I use all the time, specifically for displaying "about my app" information in either a `AlertDialog` or a `Fragment`.
 
## Usage 

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
    implementation "com.github.jonapoul:about:1.0.0"
}
```
