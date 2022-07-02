dependencies {
    api(project(":noelle-standalone:standalone-database:common-database"))
    api("org.mariadb.jdbc:mariadb-java-client:3.0.4")
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