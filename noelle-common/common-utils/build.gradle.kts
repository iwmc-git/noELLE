dependencies {
    compileOnlyApi("net.kyori:adventure-api:4.11.0")
    compileOnlyApi("net.kyori:adventure-text-minimessage:4.11.0")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "common-utils"

            from(components["java"])
        }
    }
}