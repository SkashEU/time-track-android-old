pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/9664109/artifacts/repository")
            name = "AndroidX Snapshot Repository"
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "TimeTrack"
include(":app")
include(":baselineprofile")
