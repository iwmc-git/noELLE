package noelle.features.languages.paper;

import noelle.features.languages.common.enums.Language;
import noelle.features.languages.common.enums.LanguageBackend;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.translation.Translation;
import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.paper.translation.PaperTranslation;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Locale;

public class PaperLanguages extends AbstractLanguages<Player> {

    protected PaperLanguages(Path rootDirectory, Language defaultLanguage, LanguageBackend backend, Class<?> targetClass, String includedResource) {
        super(rootDirectory, defaultLanguage, backend, targetClass, includedResource);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull PaperLanguages init(Path rootDirectory, Class<?> targetClass, Language defaultLanguage, LanguageBackend backend) {
        return new PaperLanguages(rootDirectory, defaultLanguage, backend, targetClass, "plugin.yml");
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PaperLanguages init(Path rootDirectory, Class<?> targetClass, Language defaultLanguage) {
        return PaperLanguages.init(rootDirectory, targetClass, defaultLanguage, LanguageBackend.YAML);
    }

    @Contract("_, _ -> new")
    public static @NotNull PaperLanguages init(Path rootDirectory, Class<?> targetClass) {
        return PaperLanguages.init(rootDirectory, targetClass, Language.ENGLISH_AMERICAN);
    }

    @Override
    public Locale localeFor(@NotNull Player player) {
        return player.locale();
    }

    @Override
    public Translation translationFor(@NotNull Player player, @NotNull TranslationKey key) {
        var configurationFor = configurationFor(player);
        var configKey = key.key();

        return new PaperTranslation(configurationFor.bump(configKey), player);
    }
}
