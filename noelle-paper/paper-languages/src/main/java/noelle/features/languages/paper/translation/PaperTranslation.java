package noelle.features.languages.paper.translation;

import net.kyori.adventure.text.Component;

import noelle.configuration.DefaultConfiguration;
import noelle.features.languages.common.enums.MessageType;
import noelle.features.languages.common.translation.Translation;
import noelle.features.utils.common.text.TextUtils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PaperTranslation implements Translation {
    private final DefaultConfiguration<?> configuration;
    private final Player player;

    public PaperTranslation(DefaultConfiguration<?> configuration, Player player) {
        this.configuration = configuration;
        this.player = player;
    }

    @Override
    public String rawTranslated(String... replacements) {
        var value = configuration.value(String.class);

        if (value == null || value.isEmpty() || value.isBlank()) {
            return "";
        }

        return TextUtils.colorize(value, replacements);
    }

    @Override
    public List<String> rawTranslatedList(String... replacements) {
        var value = configuration.list(String.class);

        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }

        return value.stream().map(s -> TextUtils.colorize(s, replacements)).collect(Collectors.toList());
    }

    @Override
    public Component translated(String... replacements) {
        var rawTranslated = rawTranslated(replacements);
        return TextUtils.toComponent(rawTranslated);
    }

    @Override
    public List<Component> translatedList(String... replacements) {
        var rawTranslated = rawTranslatedList(replacements);
        return rawTranslated.stream().map(TextUtils::toComponent).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(@NotNull MessageType messageType, String... replacements) {
        var translated = translated(replacements);

        switch (messageType) {
            case CHAT -> player.sendMessage(translated);
            case ACTION_BAR -> player.sendActionBar(translated);
        }
    }

    @Override
    public void sendMessageList(@NotNull MessageType messageType, String... replacements) {
        var translated = translatedList(replacements);

        switch (messageType) {
            case CHAT -> translated.forEach(player::sendMessage);
            case ACTION_BAR -> translated.forEach(player::sendActionBar);
        }
    }
}
