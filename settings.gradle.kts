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
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/0xSkash/time-track-android-api")

            credentials {
                val githubProperties = java.util.Properties()
                githubProperties.load(java.io.FileInputStream((file("github.properties"))))
                username = githubProperties.getProperty("gpr.usr") ?: System.getenv("GPR_USER")
                password = githubProperties.getProperty("gpr.key") ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}
rootProject.name = "TimeTrack"
include(":app")
include(":baselineprofile")
