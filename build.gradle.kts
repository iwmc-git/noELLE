plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "pw.iwmc.noelle"
    version = "0.2.2-SNAPSHOT"
    description = "Probably a large (really large?) collection of utilities for developing plugins."

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

        // other api repositories..
        maven { url = uri("https://repo.codemc.org/repository/maven-public/") }
        maven { url = uri("\thttps://nexus.velocitypowered.com/repository/maven-public/") }

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