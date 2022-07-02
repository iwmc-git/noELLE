package noelle.configuration.hocon;

import noelle.configuration.AbstractConfiguration;
import noelle.configuration.DefaultConfiguration;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

/**
 * Private abstract implementation for ${@link AbstractConfiguration}.
 */
final class HoconConfiguration extends AbstractConfiguration<CommentedConfigurationNode> {

    HoconConfiguration(CommentedConfigurationNode node) {
        super(node);
    }

    @Override
    public @NotNull DefaultConfiguration<CommentedConfigurationNode> bump(@NotNull String key) {
        var node = prepareNode(key);
        return new HoconConfiguration(node);
    }
}
