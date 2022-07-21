dependencies {
    compileOnlyApi(project(":noelle-common:common-messages"))

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "paper-messages"

            from(components["java"])
        }
    }
}