dependencies {
    api(project(":noelle-standalone:standalone-configuration:hocon-configuration"))
    api(project(":noelle-standalone:standalone-configuration:yaml-configuration"))

    api("org.jetbrains:annotations:23.0.0")

    api("net.kyori:adventure-api:4.11.0")
    api("net.kyori:adventure-text-minimessage:4.11.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "languages-common"

            from(components["java"])
        }
    }
}