package noelle.features.languages.paper;

import noelle.configuration.DefaultConfiguration;

import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.backend.BackendType;
import noelle.features.languages.common.key.LanguageKey;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.language.Language;
import noelle.features.languages.common.translation.Translation;
import noelle.features.languages.common.translation.ListedTranslation;
import noelle.features.languages.common.translation.DefaultTranslation;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

public class PaperLanguages extends AbstractLanguages<Player> {
    private final Map<Language, DefaultConfiguration<?>> cachedLocales;
    private final Language defaultLanguage;

    protected PaperLanguages(BackendType type, Path rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        super(type, rootPath, defaultLanguage, target, anyResource);

        this.defaultLanguage = defaultLanguage;
        this.cachedLocales = cachedLocales();
    }

    @Override
    public Translation translationFor(@NotNull Player player, @NotNull TranslationKey key) {
        var locale = localeFor(player);
        var localeFormat = locale.getLanguage() + "_" + locale.getCountry().toLowerCase(Locale.ROOT);

        var language = Language.fromKey(LanguageKey.of(localeFormat));
        var cachedConfig = language.isPresent() ? cachedLocales.get(language.get()) : cachedLocales.get(defaultLanguage);
        var configNode = cachedConfig.bump(key.key());

        try {
            return configNode.isList()
                    ? new ListedTranslation(configNode.list(String.class))
                    : new DefaultTranslation(configNode.value(String.class));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Translation translationFor(@NotNull Player player, @NotNull String key) {
        return translationFor(player, TranslationKey.of(key));
    }

    @Override
    public Locale localeFor(@NotNull Player player) {
        return player.locale();
    }

    public static @NotNull PaperLanguages init(BackendType type, @NotNull Path rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        var languages = new PaperLanguages(type, rootPath, defaultLanguage, target, anyResource);
        languages.init();

        return languages;
    }

    public static @NotNull PaperLanguages init(BackendType type, @NotNull File rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        return init(type, rootPath.toPath(), defaultLanguage, target, anyResource);
    }
}
