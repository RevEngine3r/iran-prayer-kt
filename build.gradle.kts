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

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("ir.revengine3r.prayertimes.examples.MainKt")
}