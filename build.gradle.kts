plugins {
    kotlin("multiplatform") version "1.9.24"
    id("com.android.library") version "8.2.0"
    `maven-publish`
}

group = "com.github.Nyayurn"

kotlin {
    jvmToolchain(17)
    withSourcesJar()
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.jackson.module.kotlin)
            api(libs.jsoup)
            api(libs.ktor.server.core)
            api(libs.ktor.server.cio)
            api(libs.ktor.client.core)
            api(libs.ktor.client.cio)
            api(libs.ktor.client.websockets)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.jackson)
            api(libs.kotlinx.datetime)
        }
        jvmMain.dependencies {
            api(libs.mordant)
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