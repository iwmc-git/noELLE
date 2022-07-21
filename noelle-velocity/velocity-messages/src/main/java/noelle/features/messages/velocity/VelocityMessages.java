package noelle.features.messages.velocity;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.title.Title;

import noelle.configuration.DefaultConfiguration;
import noelle.features.messages.common.AbstractMessages;
import noelle.features.messages.common.key.MessageKey;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class VelocityMessages extends AbstractMessages<Player> {

    protected VelocityMessages(DefaultConfiguration<?> configuration) {
        super(configuration);
    }

    @Contract("_ -> new")
    public static @NotNull VelocityMessages init(DefaultConfiguration<?> configuration) {
        return new VelocityMessages(configuration);
    }

    @Override
    public void sendMessage(@NotNull Player player, @NotNull MessageKey key, String... replacements) {
        var message = message(key, replacements);
        player.sendMessage(message);
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull MessageKey key, String... replacements) {
        var message = message(key, replacements);
        player.sendActionBar(message);
    }

    @Override
    public void sendTitle(@NotNull Player player, @NotNull MessageKey key, String... replacements) {
        var bump = configuration.bump(key.key());

        var fadeIn = bump.bump("fade-in").value(Long.class);
        var stay = bump.bump("stay").value(Long.class);
        var fadeOut = bump.bump("fade-out").value(Long.class);

        var title = message(MessageKey.of(key.key() + ".title"), replacements);
        var subTitle = message(MessageKey.of(key.key() + ".sub-title"), replacements);

        if (fadeIn == 0 && stay == 0 && fadeOut == 0) {
            var newTitle = Title.title(title, subTitle);

            player.showTitle(newTitle);
        } else {
            var times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
            var newTitle = Title.title(title, subTitle, times);

            player.showTitle(newTitle);
        }
    }
}
