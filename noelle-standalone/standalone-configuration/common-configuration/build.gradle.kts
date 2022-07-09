dependencies {
    compileOnlyApi("org.spongepowered:configurate-core:4.1.2")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-configuration-common"

            from(components["java"])
        }
    }
}