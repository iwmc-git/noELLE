dependencies {
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "libraries-api"

            from(components["java"])
        }
    }
}