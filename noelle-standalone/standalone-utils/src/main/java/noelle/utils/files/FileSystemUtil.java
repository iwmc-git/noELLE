package noelle.utils.files;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Used from <a href="https://github.com/PaperMC/Velocity/blob/dev/3.0.0/proxy/src/main/java/com/velocitypowered/proxy/util/FileSystemUtils.java">FileSystemUtils.java</a>
 */
public class FileSystemUtil {

    public static boolean visitResources(Class<?> target, Consumer<Path> consumer, String anyResource, String firstPathComponent, String... remainingPathComponents) throws IOException {
        var knownResource = FileSystemUtil.class.getClassLoader().getResource(anyResource);

        if (knownResource == null) {
            throw new IllegalStateException(anyResource + " does not exist, don't know where we are");
        }

        if (knownResource.getProtocol().equals("jar")) {
            var jarPathRaw = knownResource.toString().split("!")[0];
            var path = URI.create(jarPathRaw + "!/");

            try (var fileSystem = FileSystems.newFileSystem(path, Map.of("create", "true"))) {
                var toVisit = fileSystem.getPath(firstPathComponent, remainingPathComponents);

                if (Files.exists(toVisit)) {
                    consumer.accept(toVisit);
                    return true;
                }

                return false;
            }
        } else {
            URI uri;

            var componentList = new ArrayList<String>();
            componentList.add(firstPathComponent);
            componentList.addAll(Arrays.asList(remainingPathComponents));

            try {
                var url = target.getClassLoader().getResource(String.join("/", componentList));
                if (url == null) {
                    return false;
                }

                uri = url.toURI();
            } catch (URISyntaxException e) {
                throw new IllegalStateException(e);
            }

            consumer.accept(Path.of(uri));
            return true;
        }
    }

}
