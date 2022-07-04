dependencies {
    compileOnlyApi(project(":noelle-libraries:libraries-api"))
    compileOnlyApi("org.apache.maven:maven-repository-metadata:3.8.5")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "libraries-impl"

            from(components["java"])
        }
    }
}