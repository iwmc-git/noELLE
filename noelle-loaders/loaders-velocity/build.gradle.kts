import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    implementation(project(":noelle-loaders:loaders-common"))
    implementation(project(":noelle-velocity:velocity-languages"))
}

tasks {
    withType(ShadowJar::class.java) {
        val branchName = System.getProperty("BRANCH_NAME")
        val buildNumber = System.getProperty("BUILD_NUMBER")
        val gitCommit = System.getProperty("GIT_COMMIT")

        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("velocity-b${buildNumber}-${branchName}-${gitCommit}")
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