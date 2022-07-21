dependencies {
    compileOnlyApi("at.favre.lib:bcrypt:0.9.0")
    compileOnlyApi("org.bouncycastle:bcprov-jdk18on:1.71")
    compileOnlyApi("com.google.guava:guava:31.1-jre")

    compileOnly("org.jetbrains:annotations:23.0.0")
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