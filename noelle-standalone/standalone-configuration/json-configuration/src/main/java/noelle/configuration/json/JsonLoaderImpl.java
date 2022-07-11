package noelle.configuration.json;

import noelle.configuration.DefaultConfiguration;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.net.URL;

/**
 * Private implementation for ${@link JsonLoader}.
 */
final class JsonLoaderImpl implements JsonLoader {
    private final AbstractConfigurationLoader<BasicConfigurationNode> configurationLoader;
    private final DefaultConfiguration<BasicConfigurationNode> configuration;

    private BasicConfigurationNode configurationNode;

    public JsonLoaderImpl(URL url) throws ConfigurateException {
        this.configurationLoader = GsonConfigurationLoader.builder().url(url).indent(4).build();
        this.configurationNode = configurationLoader.load();

        this.configuration = new JsonConfiguration(configurationNode);
    }

    @Override
    public DefaultConfiguration<BasicConfigurationNode> configuration() {
        return configuration;
    }

    @Override
    public AbstractConfigurationLoader<BasicConfigurationNode> configurationLoader() {
        return configurationLoader;
    }

    @Override
    public BasicConfigurationNode configurationNode() {
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
