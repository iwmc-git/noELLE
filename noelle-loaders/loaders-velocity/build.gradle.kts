import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:4.0.0-SNAPSHOT")

    implementation(project(":noelle-loaders:loaders-common"))

    implementation(project(":noelle-velocity:velocity-languages"))
    implementation(project(":noelle-velocity:velocity-messages"))
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("velocity")
        archiveVersion.set(rootProject.version.toString())
    }

    withType(ProcessResources::class.java) {
        val map = mutableMapOf<String, Any>()

        map["id"] = rootProject.name.toLowerCase()
        map["name"] = rootProject.name
        map["version"] = rootProject.version.toString()
        map["description"] = rootProject.description.toString()

        filesMatching("velocity-plugin.json") {
            expand(map)
        }
    }
}