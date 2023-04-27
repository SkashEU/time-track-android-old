plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.baselineprofile")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("com.guardsquare.appsweep")
    id("com.google.gms.google-services")
}

apply(plugin = "realm-android")

android {
    namespace = "com.skash.timetrack"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.skash.timetrack"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", "\"http://192.168.178.156:8080/api\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("String", "BASE_URL", "\"http://192.168.178.156:8080/api\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        android.buildFeatures.buildConfig = true
    }
}

dependencies {

    // Hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")

    // Rx
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")
    implementation("com.jakewharton.rxbinding4:rxbinding-material:4.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.6.4")

    // Realm
    implementation("io.realm:realm-gradle-plugin:10.13.2-transformer-api")

    // API
    implementation("com.skash.timetrack.api:time-track-openapi-client:0.5.21")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Firebase
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.profileinstaller:profileinstaller:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    "baselineProfile"(project(mapOf("path" to ":baselineprofile")))
}