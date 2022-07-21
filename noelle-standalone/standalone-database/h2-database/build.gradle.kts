dependencies {
    compileOnlyApi(project(":noelle-standalone:standalone-database:common-database"))
    compileOnlyApi("com.h2database:h2:2.1.214")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-database-h2"

            from(components["java"])
        }
    }
}