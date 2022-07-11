package noelle.configuration.json;

import noelle.configuration.AbstractConfiguration;
import noelle.configuration.DefaultConfiguration;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.BasicConfigurationNode;

/**
 * Private abstract implementation for ${@link AbstractConfiguration}.
 */
final class JsonConfiguration extends AbstractConfiguration<BasicConfigurationNode> {

    JsonConfiguration(BasicConfigurationNode node) {
        super(node);
    }

    @Override
    public @NotNull DefaultConfiguration<BasicConfigurationNode> bump(@NotNull String key) {
        var node = prepareNode(key);
        return new JsonConfiguration(node);
    }
}
