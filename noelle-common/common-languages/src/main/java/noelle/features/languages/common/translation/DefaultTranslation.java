package noelle.features.languages.common.translation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DefaultTranslation implements Translation {
    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private String translated;

    public DefaultTranslation(String translated) {
        this.translated = translated;
    }

    public Component translatedComponent(String... replacements) {
        if (translated == null || translated.isEmpty() || translated.isBlank()) {
            return Component.empty();
        }

        if (replacements.length != 0) {
            for (var i = 0; i < replacements.length; i += 2) {
                translated = translated.replace(replacements[i], replacements[i + 1]);
            }
        }

        return MINI_MESSAGE.deserialize(translated.replace("&", "§"));
    }

    @Override
    public @Nullable List<Component> translatedList(String... replacements) {
        throw new UnsupportedOperationException();
    }
}
