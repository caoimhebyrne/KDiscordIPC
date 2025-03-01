val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationVersion: String by project
val log4j2Version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("maven-publish")
}

group = "dev.cbyrne"
version = "0.2.3"

sourceSets {
    create("example")
}

repositories {
    mavenCentral()
}

val exampleImplementation: Configuration by configurations
exampleImplementation.extendsFrom(configurations.implementation.get())

dependencies {
    // Add Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    // JUnix
    implementation("com.kohlschutter.junixsocket:junixsocket-core:2.6.2") // To be removed with #35

    // Logging library
    implementation("org.apache.logging.log4j:log4j-api:$log4j2Version")

    // Example implementation
    exampleImplementation(sourceSets.main.get().output)
    exampleImplementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    exampleImplementation("org.apache.logging.log4j:log4j-api:$log4j2Version")
    exampleImplementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2Version")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
