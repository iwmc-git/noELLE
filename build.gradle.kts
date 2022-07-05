plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "pw.iwmc.noelle"
    version = "1.0.1-SNAPSHOT"
    description = "Probably a large collection of utilities for developing plugins."

    java {
        withSourcesJar()
    }

    tasks {
        withType(JavaCompile::class.java) {
            options.release.set(17)
            options.encoding = "UTF-8"
        }
    }

    repositories {
        mavenCentral()

        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://maven.iwmc.pw/releases/") }
    }

    publishing {
        repositories {
            maven {
                name = "icewynd-repository"

                val releases = "https://maven.iwmc.pw/releases/"
                val snapshots = "https://maven.iwmc.pw/snapshots/"

                val finalUrl = if (rootProject.version.toString().endsWith("SNAPSHOT")) snapshots else releases

                url = uri(finalUrl)

                credentials {
                    username = System.getenv("REPO_USERNAME")
                    password = System.getenv("REPO_TOKEN")
                }
            }
        }
    }
}