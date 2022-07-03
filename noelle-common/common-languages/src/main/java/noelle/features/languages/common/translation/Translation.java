package noelle.features.languages.common.translation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import noelle.features.languages.common.Language;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Translation {
    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final Language language;
    private final List<String> translated;

    public Translation(Language language, List<String> translated) {
        this.language = language;
        this.translated = translated;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Translation of(Language language, List<String> translated) {
        return new Translation(language, translated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Translation of(Language language, String translated) {
        return new Translation(language, List.of(translated));
    }

    public void format(Object... values) {
        translated.replaceAll(s -> String.format(Locale.ROOT, s, values));
    }

    public Language language() {
        return language;
    }

    public List<String> rawTranslated() {
        return translated;
    }

    public List<Component> translated() {
        return translated.stream().map(MINI_MESSAGE::deserialize).collect(Collectors.toList());
    }
}
