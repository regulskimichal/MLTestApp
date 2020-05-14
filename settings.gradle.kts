rootProject.name = "ML Test App"
include(":app")

pluginManagement {
    val gradleAndroidPluginVersion: String by settings
    val kotlinVersion: String by settings
    val googleServicesVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:$gradleAndroidPluginVersion")
                "org.jetbrains.kotlin.android" -> useModule("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:$kotlinVersion")
                "org.jetbrains.kotlin.android.extensions" -> useModule("org.jetbrains.kotlin.android.extensions:org.jetbrains.kotlin.android.extensions.gradle.plugin:$kotlinVersion")
                "org.jetbrains.kotlin.kapt" -> useModule("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:$kotlinVersion")
                "com.google.gms.google-services" -> useModule("com.google.gms:google-services:$googleServicesVersion")
            }
        }
    }
}
