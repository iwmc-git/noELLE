package noelle.configuration.types.json;

import noelle.configuration.DefaultConfiguration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.net.URL;
import java.nio.file.Path;

public interface JsonLoader {

    /**
     * Returns loaded configuration.
     */
    DefaultConfiguration<BasicConfigurationNode> configuration();

    /**
     * Returns configuration loader for ${@link DefaultConfiguration}.
     */
    AbstractConfigurationLoader<BasicConfigurationNode> configurationLoader();

    /**
     * Returns configuration node for ${@link DefaultConfiguration}.
     */
    BasicConfigurationNode configurationNode();

    /**
     * Saving configuration.
     *
     * @param reload need reload configuration?
     */
    boolean save(boolean reload);

    /**
     * Loads new configuration by specified path.
     *
     * @param path configuration path.
     * @return loaded configuration.
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull JsonLoader loader(Path path) {
        try {
            var url = path.toUri().toURL();
            return loader(url);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Loads new configuration by specified url.
     *
     * @param url configuration url.
     * @return loaded configuration.
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull JsonLoader loader(URL url) {
        try {
            return new JsonLoaderImpl(url);
        } catch (ConfigurateException exception) {
            throw new RuntimeException(exception);
        }
    }
}
