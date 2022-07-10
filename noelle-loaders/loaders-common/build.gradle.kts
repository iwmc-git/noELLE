import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.9.0")
    compileOnly("org.jetbrains:annotations:23.0.0")

    implementation(project(":noelle-standalone:standalone-configuration:common-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:hocon-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:yaml-configuration"))
    implementation(project(":noelle-standalone:standalone-database:common-database"))
    implementation(project(":noelle-standalone:standalone-database:h2-database"))
    implementation(project(":noelle-standalone:standalone-database:mariadb-database"))
    implementation(project(":noelle-standalone:standalone-encryptor"))
    implementation(project(":noelle-standalone:standalone-scheduler"))
    implementation(project(":noelle-standalone:standalone-utils"))

    implementation(project(":noelle-common:common-languages"))

    implementation("pw.iwmc.libman:libman-api:1.0.5") {
        exclude("org.slf4j", "slf4j-api")
        exclude("org.slf4j", "slf4j-simple")
    }

    implementation("pw.iwmc.libman:libman-common:1.0.5") {
        exclude("org.slf4j", "slf4j-api")
        exclude("org.slf4j", "slf4j-simple")
    }
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("common")
        archiveVersion.set(rootProject.version.toString())
    }
}