plugins {
    kotlin("jvm") version "1.9.23"
    `maven-publish`
    java
}

group = "com.github.Nyayurn"

val jackson_version = "2.17.0"
val jsoup_version = "1.17.2"
val ktor_version = "2.3.9"
val junit_version = "5.10.1"
val mordant_version = "2.3.0"

repositories {
    mavenCentral()
}

dependencies {
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
    api("org.jsoup:jsoup:$jsoup_version")
    api("io.ktor:ktor-server-core:$ktor_version")
    api("io.ktor:ktor-server-cio:$ktor_version")
    api("io.ktor:ktor-client-core:$ktor_version")
    api("io.ktor:ktor-client-cio:$ktor_version")
    api("io.ktor:ktor-client-websockets:$ktor_version")
    api("io.ktor:ktor-serialization-jackson:$ktor_version")
    api("com.github.ajalt.mordant:mordant:$mordant_version")
    testImplementation("org.junit.jupiter:junit-jupiter:$junit_version")
}

sourceSets {
    main {
        java.srcDir("main")
    }
    test {
        java.srcDir("test")
    }
}

java {
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

publishing.publications.create<MavenPublication>("maven") {
    from(components["java"])
}

kotlin {
    jvmToolchain(17)
}