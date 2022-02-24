plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"

    `maven-publish`
}

group = "dev.cbyrne"
version = "1.0.0"

repositories {
    mavenCentral()
}

sourceSets {
    create("example")
}

val exampleImplementation by configurations
exampleImplementation.extendsFrom(configurations.implementation.get())

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization)

    implementation(libs.junixsocket.core)
    implementation(libs.slf4j.api)

    exampleImplementation(sourceSets.main.get().output)

    // Log4J is only used in the example project as a backend for SLF4j
    exampleImplementation(libs.log4j.core)
    exampleImplementation(libs.log4j.api)
    exampleImplementation(libs.log4j.slf4j)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
}
