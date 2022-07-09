dependencies {
    compileOnlyApi("com.zaxxer:HikariCP:5.0.1")
    compileOnlyApi("me.lucko:sql-streams:1.0.0")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-database-common"

            from(components["java"])
        }
    }
}