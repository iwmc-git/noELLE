package noelle.loaders.common.utils;

import com.google.gson.Gson;

import noelle.loaders.common.CommonLoader;
import noelle.loaders.common.config.CustomConfiguration;
import noelle.loaders.common.config.DefaultConfiguration;
import noelle.loaders.common.config.LoaderConfiguration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationUtil {
    private final static Gson GSON = new Gson();
    private final static Path ROOT = CommonLoader.loader().root();

    public static DefaultConfiguration loadDefaultConfig() {
        var resource = resource("libraries-common.json");

        if (resource == null) {
            throw new RuntimeException("Default resource file not found!");
        }

        return GSON.fromJson(new InputStreamReader(resource), DefaultConfiguration.class);
    }

    public static LoaderConfiguration loadLoaderConfig() {
        try {
            var resource = resource("configuration.json");

            if (resource == null) {
                throw new RuntimeException("Default resource file not found!");
            }

            var configFile = ROOT.resolve("configuration.json");
            if (Files.notExists(configFile)) {
                Files.copy(resource, configFile);
            }

            return GSON.fromJson(new FileReader(configFile.toFile()), LoaderConfiguration.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static CustomConfiguration loadCustomConfig(String fileName) {
        var resource = resource(fileName);

        if (resource == null) {
            throw new RuntimeException("Default resource file not found!");
        }

        return GSON.fromJson(new InputStreamReader(resource), CustomConfiguration.class);
    }

    public static @Nullable InputStream resource(@NotNull String name) {
        return ConfigurationUtil.class.getClassLoader().getResourceAsStream(name);
    }

}
