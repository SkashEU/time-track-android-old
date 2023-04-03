// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.13.2-transformer-api")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

plugins {
    id("com.android.application") version "8.1.0-alpha11" apply false
    id("com.android.library") version "8.1.0-alpha11" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.test") version "8.1.0-alpha11" apply false
    id("androidx.baselineprofile") version "1.2.0-SNAPSHOT" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("org.jetbrains.kotlin.kapt") version "1.8.10"
}