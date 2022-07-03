dependencies {
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
    compileOnlyApi("at.favre.lib:bcrypt:0.9.0")
    compileOnlyApi("org.bouncycastle:bcprov-jdk15on:1.70")
    compileOnlyApi("com.google.guava:guava:31.1-jre")
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