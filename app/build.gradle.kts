import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature.PARCELIZE

val kotlinVersion: String by project
val koinVersion: String by project
val gradleAndroidPluginVersion: String by project

plugins {
    id("com.android.application")
    id("koin")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "pl.michalregulski.firebaseml"
        minSdkVersion(28)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    androidExtensions {
        features = setOf(PARCELIZE.featureName)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")

    implementation("com.google.android.material:material:1.1.0")

    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    implementation("androidx.navigation:navigation-fragment:2.2.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation("androidx.navigation:navigation-ui:2.2.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")

    kapt("androidx.databinding:databinding-compiler:$gradleAndroidPluginVersion")

    implementation("org.koin:koin-android:$koinVersion")
    implementation("org.koin:koin-androidx-scope:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    implementation("org.koin:koin-androidx-fragment:$koinVersion")

    implementation("com.google.firebase:firebase-analytics:17.3.0")
    implementation("com.google.firebase:firebase-ml-vision:24.0.2")
    implementation("com.google.firebase:firebase-ml-vision-image-label-model:20.0.0")

    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
}
