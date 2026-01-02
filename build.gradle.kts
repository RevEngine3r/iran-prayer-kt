plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "ir.revengine3r"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// Set Java compatibility to 1.8 (Java 8) - compatible with most systems
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// Configure Kotlin to target JVM 1.8
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

application {
    mainClass.set("ir.revengine3r.prayertimes.examples.MainKt")
}