package noelle.features.messages.common;

import net.kyori.adventure.text.Component;

import noelle.configuration.DefaultConfiguration;
import noelle.features.messages.common.key.MessageKey;
import noelle.features.utils.common.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMessages<P> {
    protected final DefaultConfiguration<?> configuration;

    protected AbstractMessages(DefaultConfiguration<?> configuration) {
        this.configuration = configuration;
    }

    public Component message(MessageKey key, String... replacements) {
        return TextUtils.toComponent(rawMessage(key, replacements));
    }

    public String rawMessage(@NotNull MessageKey key, String... replacements) {
        var message = configuration.bump(key.key()).value(String.class);

        if (message == null || message.isEmpty() || message.isBlank()) {
            return "";
        }

        return TextUtils.colorize(message, replacements);
    }

    public List<Component> listedMessage(MessageKey key, String... replacements) {
        return rawListedMessage(key, replacements).stream().map(s -> TextUtils.toComponent(s, replacements)).collect(Collectors.toList());
    }

    public List<String> rawListedMessage(@NotNull MessageKey key, String... replacements) {
        var message = configuration.bump(key.key()).list(String.class);

        if (message == null || message.isEmpty()) {
            return Collections.emptyList();
        }

        return message.stream().map(s -> TextUtils.colorize(s, replacements)).collect(Collectors.toList());
    }

    public abstract void sendMessage(P player, MessageKey key, String... replacements);

    public void sendMessage(P player, String key, String... replacements) {
        sendMessage(player, MessageKey.of(key), replacements);
    }

    public abstract void sendActionBar(P player, MessageKey key, String... replacements);

    public void sendActionBar(P player, String key, String... replacements) {
        sendMessage(player, MessageKey.of(key), replacements);
    }

    public abstract void sendTitle(P player, MessageKey key, String... replacements);

    public void sendTitle(P player, String key, String... replacements) {
        sendTitle(player, MessageKey.of(key), replacements);
    }
}
