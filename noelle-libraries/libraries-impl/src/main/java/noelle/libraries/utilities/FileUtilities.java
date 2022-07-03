package noelle.libraries.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtilities {

    public static void makeDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                Files.delete(path);
            }
        } else {
            Files.createDirectories(path);
        }
    }
}
