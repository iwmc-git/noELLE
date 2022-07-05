import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.9.0")

    implementation("pw.iwmc.libman:libman-api:1.0.0") {
        exclude("org.jetbrains", "annotations")
        exclude("org.slf4j", "slf4j-api")
        exclude("org.slf4j", "slf4j-impl")
    }

    implementation("pw.iwmc.libman:libman-common:1.0.0") {
        exclude("org.jetbrains", "annotations")
        exclude("org.slf4j", "slf4j-api")
        exclude("org.slf4j", "slf4j-impl")
    }
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("common")
        archiveVersion.set(rootProject.version.toString())
    }
}