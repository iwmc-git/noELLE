package noelle.loaders.common;

import com.google.gson.Gson;
import noelle.loaders.common.objects.JsonConfiguration;
import noelle.loaders.common.objects.JsonObjects;
import noelle.loaders.common.utils.JsonObjectsUtil;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.api.LibmanAPI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommonLoader {
    private final LibmanAPI libmanAPI;
    private final JsonObjects jsonObjects;

    private final Path root;

    public CommonLoader(Path root) {
        this.jsonObjects = JsonObjectsUtil.objects("libraries-common.json", getClass());
        this.root = root;

        var config = loadConfig();

        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        var repositories = jsonObjects.repositories();
        this.libmanAPI = new Libman(root, repositories, config.isDebug(), config.isCheckFileHash());
    }

    public void start() {
        var dependencies = jsonObjects.dependencies();
        var downloader = libmanAPI.downloader();

        dependencies.forEach(downloader::downloadDependency);
    }

    public List<Path> downloaded() {
        return new ArrayList<>(libmanAPI.downloaded().values());
    }

    private JsonConfiguration loadConfig() {
        try {
            var resource = getClass().getResourceAsStream("configuration.json");
            if (resource == null) {
                throw new RuntimeException("configuration.json not found!");
            }

            var file = root.resolve("configuration.json");

            if (Files.notExists(file)) {
                Files.copy(resource, file);
            }

            var reader = new InputStreamReader(resource);
            return new Gson().fromJson(reader, JsonConfiguration.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
