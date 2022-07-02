package noelle.configuration.hocon;

import noelle.configuration.DefaultConfiguration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.net.URL;
import java.nio.file.Path;

public interface HoconLoader {

    /**
     * Returns loaded configuration.
     */
    DefaultConfiguration<CommentedConfigurationNode> configuration();

    /**
     * Returns configuration loader for ${@link DefaultConfiguration}.
     */
    AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader();

    /**
     * Returns configuration node for ${@link DefaultConfiguration}.
     */
    CommentedConfigurationNode configurationNode();

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
    static @NotNull HoconLoader loader(Path path) {
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
    static @NotNull HoconLoader loader(URL url) {
        try {
            return new HoconLoaderImpl(url);
        } catch (ConfigurateException exception) {
            throw new RuntimeException(exception);
        }
    }
}
