package noelle.loaders.common;

import noelle.loaders.common.objects.JsonObjects;
import noelle.loaders.common.utils.JsonObjectsUtil;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.api.LibmanAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommonLoader {
    private final LibmanAPI libmanAPI;
    private final JsonObjects jsonObjects;

    public CommonLoader(Path root, boolean checkHash, boolean enableLogger) {
        this.jsonObjects = JsonObjectsUtil.objects("libraries-common.json", getClass().getClassLoader());

        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        var repositories = jsonObjects.repositories();
        this.libmanAPI = new Libman(root, repositories, enableLogger, checkHash);
    }

    public void start() {
        var dependencies = jsonObjects.dependencies();
        var downloader = libmanAPI.downloader();

        dependencies.forEach(downloader::downloadDependency);
    }

    public List<Path> downloaded() {
        return new ArrayList<>(libmanAPI.downloaded().values());
    }
}
