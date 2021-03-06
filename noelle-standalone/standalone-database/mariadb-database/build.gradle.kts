dependencies {
    compileOnlyApi(project(":noelle-standalone:standalone-database:common-database"))
    compileOnlyApi("org.mariadb.jdbc:mariadb-java-client:3.0.6")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-database-mariadb"

            from(components["java"])
        }
    }
}