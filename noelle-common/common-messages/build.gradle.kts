dependencies {
    compileOnlyApi(project(":noelle-standalone:standalone-configuration:common-configuration"))
    compileOnlyApi(project(":noelle-common:common-utils"))

    compileOnlyApi("net.kyori:adventure-api:4.11.0")
    compileOnlyApi("net.kyori:adventure-text-minimessage:4.11.0")

    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "common-messages"

            from(components["java"])
        }
    }
}