dependencies {
    compileOnlyApi("com.google.guava:guava:31.1-jre")

    compileOnlyApi("org.slf4j:slf4j-api:1.7.36")
    compileOnlyApi("org.slf4j:slf4j-simple:1.7.36")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-scheduler"

            from(components["java"])
        }
    }
}