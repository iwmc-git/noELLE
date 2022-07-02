package noelle.configuration.yaml;

import noelle.configuration.AbstractConfiguration;
import noelle.configuration.DefaultConfiguration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

/**
 * Private abstract implementation for ${@link AbstractConfiguration}.
 */
final class YamlConfiguration extends AbstractConfiguration<CommentedConfigurationNode> {

    YamlConfiguration(CommentedConfigurationNode node) {
        super(node);
    }

    @Override
    public @NotNull DefaultConfiguration<CommentedConfigurationNode> bump(@NotNull String key) {
        var node = prepareNode(key);
        return new YamlConfiguration(node);
    }
}
