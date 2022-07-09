dependencies {
    compileOnlyApi(project(":noelle-standalone:standalone-configuration:common-configuration"))
    compileOnlyApi("org.spongepowered:configurate-hocon:4.1.2")

    compileOnly("org.jetbrains:annotations:23.0.0")
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