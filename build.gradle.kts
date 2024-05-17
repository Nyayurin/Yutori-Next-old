plugins {
    kotlin("multiplatform") version "1.9.24"
    id("com.android.library") version "8.2.0"
    `maven-publish`
}

group = "com.github.Nyayurn"

val jackson_version = "2.17.0"
val jsoup_version = "1.17.2"
val ktor_version = "2.3.9"
val junit_version = "5.10.1"
val mordant_version = "2.3.0"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    withSourcesJar()
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }
    sourceSets {
        jvmMain.dependencies {
            api("com.github.ajalt.mordant:mordant:$mordant_version")
        }
        commonMain.dependencies {
            api("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
            api("org.jsoup:jsoup:$jsoup_version")
            api("io.ktor:ktor-server-core:$ktor_version")
            api("io.ktor:ktor-server-cio:$ktor_version")
            api("io.ktor:ktor-client-core:$ktor_version")
            api("io.ktor:ktor-client-cio:$ktor_version")
            api("io.ktor:ktor-client-websockets:$ktor_version")
            api("io.ktor:ktor-serialization-jackson:$ktor_version")
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