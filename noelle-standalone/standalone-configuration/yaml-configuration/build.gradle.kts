dependencies {
    api(project(":noelle-standalone:standalone-configuration:common-configuration"))
    api("org.spongepowered:configurate-yaml:4.1.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-configuration-yaml"

            from(components["java"])
        }
    }
}