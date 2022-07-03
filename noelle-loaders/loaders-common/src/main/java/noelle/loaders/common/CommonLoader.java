package noelle.loaders.common;

import noelle.libraries.LibrariesLoader;
import noelle.libraries.api.injector.Injector;
import noelle.loaders.common.objects.JsonObjects;
import noelle.loaders.common.utils.JsonObjectsUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommonLoader {
    private final LibrariesLoader librariesLoader;
    private final JsonObjects jsonObjects;
    private final Injector injector;

    public CommonLoader(Path root, Injector injector, boolean checkHash) {
        this.injector = injector;
        this.jsonObjects = JsonObjectsUtil.objects("libraries-common.json", getClass().getClassLoader());

        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        var repositories = jsonObjects.repositories();
        this.librariesLoader = new LibrariesLoader(root, repositories, checkHash);
    }

    public void downloadBase() {
        for (var library : jsonObjects.libraries()) {
            librariesLoader.download(library);
            librariesLoader.inject(library, injector);
        }
    }

    public void shutdown() {
        librariesLoader.stop();
    }
}
