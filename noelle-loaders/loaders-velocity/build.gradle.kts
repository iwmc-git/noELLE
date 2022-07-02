import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    implementation(project(":noelle-standalone:standalone-configuration:common-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:hocon-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:yaml-configuration"))
    implementation(project(":noelle-standalone:standalone-database:common-database"))
    implementation(project(":noelle-standalone:standalone-database:h2-database"))
    implementation(project(":noelle-standalone:standalone-database:mariadb-database"))
    implementation(project(":noelle-standalone:standalone-encryptor"))
    implementation(project(":noelle-standalone:standalone-scheduler"))
    implementation(project(":noelle-standalone:standalone-utils"))
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set(rootProject.version.toString())
    }

    withType(ProcessResources::class.java) {
        val map = mutableMapOf<String, Any>()

        map["plugin.id"] = rootProject.name.toLowerCase()
        map["plugin.name"] = rootProject.name
        map["plugin.version"] = rootProject.version.toString()
        map["plugin.description"] = rootProject.description.toString()

        filesMatching("velocity-plugin.json") {
            expand(map)
        }
    }
}