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

    exampleImplementation(sourceSets.main.get().output)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
