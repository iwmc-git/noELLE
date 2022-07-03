import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2" apply(true)
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation(project(":noelle-libraries:libraries-api"))
    implementation("org.apache.maven:maven-repository-metadata:3.8.5")

    implementation(project(":noelle-libraries:libraries-api"))
    implementation(project(":noelle-libraries:libraries-impl"))

    implementation(project(":noelle-standalone:standalone-configuration:common-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:hocon-configuration"))
    implementation(project(":noelle-standalone:standalone-configuration:yaml-configuration"))
    implementation(project(":noelle-standalone:standalone-database:common-database"))
    implementation(project(":noelle-standalone:standalone-database:h2-database"))
    implementation(project(":noelle-standalone:standalone-database:mariadb-database"))
    implementation(project(":noelle-standalone:standalone-encryptor"))
    implementation(project(":noelle-standalone:standalone-scheduler"))
    implementation(project(":noelle-standalone:standalone-utils"))

    implementation(project(":noelle-loaders:loaders-common"))
    implementation(project(":noelle-paper:paper-languages"))
}

tasks {
    withType(ShadowJar::class.java) {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("paper")
        archiveVersion.set(rootProject.version.toString())

        relocate("org.apache.maven.artifact.repository.metadata", "noelle.builtin-libs.maven-metadata")
        relocate("org.codehaus.plexus", "noelle.builtin-libs.plexus")
    }

    withType(ProcessResources::class.java) {
        val map = mutableMapOf<String, Any>()

        map["name"] = rootProject.name
        map["version"] = rootProject.version.toString()
        map["description"] = rootProject.description.toString()

        filesMatching("plugin.yml") {
            expand(map)
        }
    }
}