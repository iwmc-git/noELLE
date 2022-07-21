package noelle.configuration.types.yaml;

import noelle.configuration.DefaultConfiguration;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.net.URL;

/**
 * Private implementation for ${@link YamlLoader}.
 */
final class YamlLoaderImpl implements YamlLoader {
    private final AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private final DefaultConfiguration<CommentedConfigurationNode> configuration;

    private CommentedConfigurationNode configurationNode;

    public YamlLoaderImpl(URL url) throws ConfigurateException {
        this.configurationLoader = YamlConfigurationLoader.builder().url(url).indent(4).nodeStyle(NodeStyle.BLOCK).build();
        this.configurationNode = configurationLoader.load();

        this.configuration = new YamlConfiguration(configurationNode);
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
