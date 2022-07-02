package noelle.configuration.hocon;

import noelle.configuration.DefaultConfiguration;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.net.URL;

/**
 * Private implementation for ${@link HoconLoader}.
 */
final class HoconLoaderImpl implements HoconLoader {
    private final AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private final DefaultConfiguration<CommentedConfigurationNode> configuration;

    private CommentedConfigurationNode configurationNode;

    public HoconLoaderImpl(URL url) throws ConfigurateException {
        this.configurationLoader = HoconConfigurationLoader.builder().url(url).prettyPrinting(true).build();
        this.configurationNode = configurationLoader.load();

        this.configuration = new HoconConfiguration(configurationNode);
    }

    @Override
    public DefaultConfiguration<CommentedConfigurationNode> configuration() {
        return configuration;
    }

    @Override
    public AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader() {
        return configurationLoader;
    }

    @Override
    public CommentedConfigurationNode configurationNode() {
        return configurationNode;
    }

    @Override
    public boolean save(boolean reload) {
        try {
            configurationLoader.save(configurationNode);

            if (reload) {
                this.configurationNode = configurationLoader.load();
            }

            return true;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
