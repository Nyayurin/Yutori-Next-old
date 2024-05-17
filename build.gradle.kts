plugins {
    kotlin("multiplatform") version "1.9.24"
    id("com.android.library") version "8.2.0"
    `maven-publish`
}

group = "com.github.Nyayurn"

val ktor_version = "2.3.9"

kotlin {
    jvmToolchain(17)
    withSourcesJar()
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }
    sourceSets {
        jvmMain.dependencies {
            api(libs.mordant)
        }
        commonMain.dependencies {
            api(libs.jackson.module.kotlin)
            api(libs.jsoup)
            api(libs.ktor.server.core)
            api(libs.ktor.server.cio)
            api(libs.ktor.client.core)
            api(libs.ktor.client.cio)
            api(libs.ktor.client.websockets)
            api(libs.ktor.serialization.jackson)
        }
        androidMain.dependencies {
        }
    }
}

android {
    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}