dependencies {
    compileOnlyApi(project(":noelle-common:common-utils"))

    compileOnlyApi("de.tr7zw:item-nbt-api-plugin:2.10.0")

    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "paper-inventories"

            from(components["java"])
        }
    }
}