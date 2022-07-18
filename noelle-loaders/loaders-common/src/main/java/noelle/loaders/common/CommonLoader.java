package noelle.loaders.common;

import noelle.loaders.common.utils.ConfigurationUtil;
import noelle.loaders.common.config.DefaultConfiguration;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.api.LibmanAPI;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommonLoader {
    private static CommonLoader loader;

    private final Path root;
    private final LibmanAPI libman;
    private final DefaultConfiguration defaultConfig;

    public CommonLoader(Path root) {
        this.root = root;

        loader = this;

        try {
            var loaderConfig = ConfigurationUtil.loadLoaderConfig();
            this.defaultConfig = ConfigurationUtil.loadDefaultConfig();

            if (defaultConfig.dependencies().isEmpty()) {
                throw new RuntimeException("Dependencies from libraries-common.json not found!");
            }

            if (defaultConfig.repositories().isEmpty()) {
                throw new RuntimeException("Repositories from libraries-common.json not found!");
            }

            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }

            this.libman = new Libman(
                    root,
                    defaultConfig.repositories(),
                    loaderConfig.debug(),
                    loaderConfig.checkFileHash(),
                    loaderConfig.useRemapper()
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void downloadDefaults() {
        var dependencies = defaultConfig.dependencies();
        var downloader = libman.downloader();

        dependencies.forEach(downloader::downloadDependency);
    }

    public void downloadFromCustom(String fileName) {
        var customConfig = ConfigurationUtil.loadCustomConfig(fileName);

        if (customConfig.dependencies().isEmpty()) {
            throw new RuntimeException("Dependencies from " + fileName + " not found!");
        }

        var dependencies = customConfig.dependencies();
        var downloader = libman.downloader();

        dependencies.forEach(downloader::downloadDependency);
    }

    public static CommonLoader loader() {
        return loader;
    }

    public Path root() {
        return root;
    }
}
