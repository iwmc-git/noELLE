dependencies {
    api("org.jetbrains:annotations:23.0.0")
    api("at.favre.lib:bcrypt:0.9.0")
    api("org.bouncycastle:bcprov-jdk15on:1.70")
    api("com.google.guava:guava:31.1-jre")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "standalone-encryptor"

            from(components["java"])
        }
    }
}