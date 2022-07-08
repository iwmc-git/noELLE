import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    implementation(project(":noelle-loaders:loaders-common"))
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("velocity")
        archiveVersion.set(rootProject.version.toString())

        relocate("org.apache.maven.artifact.repository.metadata", "noelle.builtin-libs.maven-metadata")
        relocate("org.codehaus.plexus", "noelle.builtin-libs.plexus")
        relocate("org.intellij.lang.annotations", "noelle.builtin-libs.annotations")
        relocate("org.jetbrains.annotations", "noelle.builtin-libs.annotations")
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