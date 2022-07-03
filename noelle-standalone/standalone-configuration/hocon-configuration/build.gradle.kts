dependencies {
    api(project(":noelle-standalone:standalone-configuration:common-configuration"))
    api("org.spongepowered:configurate-hocon:4.1.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-configuration-hocon"

            from(components["java"])
        }
    }
}