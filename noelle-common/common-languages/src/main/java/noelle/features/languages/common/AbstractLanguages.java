package noelle.features.languages.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import noelle.configuration.DefaultConfiguration;
import noelle.configuration.hocon.HoconLoader;
import noelle.configuration.json.JsonLoader;
import noelle.configuration.yaml.YamlLoader;

import noelle.features.languages.common.key.LanguageKey;
import noelle.features.languages.common.translation.DefaultTranslation;
import noelle.features.languages.common.translation.ListedTranslation;
import noelle.utils.files.FileSystemUtil;

import noelle.features.languages.common.backend.BackendType;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.language.Language;
import noelle.features.languages.common.translation.Translation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractLanguages<P> {
    private final Map<Language, DefaultConfiguration<?>> cachedLocales;

    private final BackendType type;
    private final Path rootPath;
    private final Language defaultLanguage;
    private final Class<?> target;
    private final String anyResource;

    public AbstractLanguages(BackendType type, Path rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        this.type = type;
        this.rootPath = rootPath;
        this.defaultLanguage = defaultLanguage;
        this.target = target;
        this.anyResource = anyResource;

        this.cachedLocales = new HashMap<>();
    }

    public void init() {
        var languagesDir = rootPath.resolve("languages");

        try {
            if (Files.notExists(rootPath)) {
                Files.createDirectory(rootPath);
            }

            if (Files.notExists(languagesDir)) {
                Files.createDirectory(languagesDir);

                extractLanguages(anyResource);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        for (var language : Language.values()) {
            DefaultConfiguration<?> configuration = null;

            var languageFileFormat = language.key().code() + type.extension();
            var localeFile = languagesDir.resolve(languageFileFormat);

            if (Files.exists(localeFile)) {
                switch (type) {
                    case HOCON -> configuration = HoconLoader.loader(localeFile).configuration();
                    case YAML -> configuration = YamlLoader.loader(localeFile).configuration();
                    case JSON -> configuration = JsonLoader.loader(localeFile).configuration();
                }

                cachedLocales.put(language, configuration);
            }
        }

        if (!cachedLocales.containsKey(defaultLanguage)) {
            throw new RuntimeException("Default configuration not created! Default language: " + defaultLanguage.key().code());
        }
    }

    private void extractLanguages(String anyResource) throws Exception {
        var langPath = rootPath.resolve("languages");

        FileSystemUtil.visitResources(target, path -> {
            try (var stream = Files.walk(path).filter(Files::isRegularFile)) {
                stream.forEach(file -> {
                    var langFile = langPath.resolve(file.getFileName().toString());
                    if (!Files.exists(langFile)) {
                        try (var inputStream = Files.newInputStream(file)) {
                            Files.copy(inputStream, langFile);
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, anyResource, "languages");
    }

    public Translation translationFor(@NotNull P player, @NotNull TranslationKey key) {
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

    public Translation translationFor(@NotNull P player, @NotNull String key) {
        return translationFor(player, key);
    }

    public abstract Locale localeFor(@NotNull P player);

    public Map<Language, DefaultConfiguration<?>> cachedLocales() {
        return cachedLocales;
    }
}
