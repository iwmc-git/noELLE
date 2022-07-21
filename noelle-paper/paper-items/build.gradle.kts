dependencies {
    compileOnlyApi(project(":noelle-common:common-utils"))

    compileOnlyApi("de.tr7zw:item-nbt-api-plugin:2.10.0")
    compileOnlyApi("com.mojang:authlib:3.5.41")

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "paper-items"

            from(components["java"])
        }
    }
}