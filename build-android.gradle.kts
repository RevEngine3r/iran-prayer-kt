// Custom build file for iran-prayer-kt when used as submodule in MyGod
// This avoids plugin version conflicts by not specifying Kotlin version

plugins {
    kotlin("jvm") // No version - inherits from parent project
}

group = "ir.revengine3r"
version = "1.0.0"

// Match MyGod project's Java version
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
    
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}
