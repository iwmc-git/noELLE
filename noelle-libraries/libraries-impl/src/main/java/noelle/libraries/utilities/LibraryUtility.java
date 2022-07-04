package noelle.libraries.utilities;

import noelle.libraries.api.objects.Library;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LibraryUtility {

    public static @NotNull File libraryFile(@NotNull Library library) {
        return libraryPath(library).toFile();
    }

    public static @NotNull File libraryFile(@NotNull Library library, @NotNull Path root) {
        return libraryPath(library, root).toFile();
    }

    public static @NotNull Path libraryPath(@NotNull Library library) {
        return Paths.get(library.artifactName() + ".jar");
    }

    public static @NotNull Path libraryPath(@NotNull Library library, @NotNull Path root) {
        return root.resolve(libraryPath(library));
    }

    public static @NotNull URL sha1Url(@NotNull Library library, String root) {
        try {
            var uri = baseUrl(library, root).toURI();
            var path = uri.getPath() + "/" + URLEncoder.encode(library.artifactName(), StandardCharsets.UTF_8) + ".jar.sha1";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create artifact base url for: " + library.artifactId(), e);
        }
    }

    public static @NotNull URL baseUrl(@NotNull Library library, @NotNull String root) {
        try {
            var uri = new URL(root).toURI();

            var path = String.join(
                    "/", uri.getPath(),
                    library.groupId().replace(".", "/"),
                    library.artifactId().replace(".", "/"),
                    URLEncoder.encode(library.version(), StandardCharsets.UTF_8)
            );

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create base url for: " + library.artifactId(), e);
        }
    }

    public static @NotNull URL artifactUrl(@NotNull Library library, @NotNull String root) {
        try {
            var uri = baseUrl(library, root).toURI();
            var path = uri.getPath() + "/" + URLEncoder.encode(library.artifactName(), StandardCharsets.UTF_8) + ".jar";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create artifact base url for: " + library.artifactId(), e);
        }
    }

    public static @NotNull URL metadataUrl(@NotNull Library library, @NotNull String root) {
        try {
            var uri = baseUrl(library, root).toURI();
            var path = uri.getPath() + "/maven-metadata.xml";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create maven-metadata base url for: " + library.artifactId(), e);
        }
    }

    public static @NotNull File metadataFile(@NotNull Library library, @NotNull String root) {
        var replacedGroupId = library.groupId().replace(".", "/");
        var replacedArtifactId = library.artifactId().replace(".", "/");
        var version = library.version();

        return Paths.get(root).resolve(replacedGroupId).resolve(replacedArtifactId).resolve(version).resolve("maven-metadata.xml").toFile();
    }
}
