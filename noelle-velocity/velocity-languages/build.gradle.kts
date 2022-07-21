dependencies {
    compileOnlyApi(project(":noelle-common:common-languages"))

    compileOnly("com.velocitypowered:velocity-api:4.0.0-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "velocity-languages"

            from(components["java"])
        }
    }
}