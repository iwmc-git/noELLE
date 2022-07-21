package noelle.features.languages.common;

import noelle.configuration.DefaultConfiguration;
import noelle.configuration.types.hocon.HoconLoader;
import noelle.configuration.types.json.JsonLoader;
import noelle.configuration.types.yaml.YamlLoader;

import noelle.features.languages.common.enums.LanguageBackend;
import noelle.features.languages.common.key.LanguageKey;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.enums.Language;
import noelle.features.languages.common.translation.Translation;

import noelle.utils.files.FileSystemUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractLanguages<P> {
    private final Map<Language, DefaultConfiguration<?>> cachedLocales;

    private final Path rootPath;
    private final Language defaultLanguage;
    private final Class<?> target;
    private final String anyResource;

    public AbstractLanguages(Path rootDirectory, Language defaultLanguage, LanguageBackend backend, Class<?> targetClass, String includedResource) {
        this.rootPath = rootDirectory;
        this.defaultLanguage = defaultLanguage;
        this.target = targetClass;
        this.anyResource = includedResource;

        this.cachedLocales = new HashMap<>();

        try {
            var languagesDir = rootPath.resolve("languages");

            if (Files.notExists(rootPath)) {
                Files.createDirectory(rootPath);
            }

            if (Files.notExists(languagesDir)) {
                Files.createDirectory(languagesDir);

                extractLanguages();
            }

            for (var language : Language.values()) {
                var languageFileFormat = language.key().code() + backend.extension();
                var localeFile = languagesDir.resolve(languageFileFormat);

                if (Files.exists(localeFile)) {
                    var configuration = switch (backend) {
                        case HOCON -> HoconLoader.loader(localeFile).configuration();
                        case YAML -> YamlLoader.loader(localeFile).configuration();
                        case JSON -> JsonLoader.loader(localeFile).configuration();
                    };

                    cachedLocales.put(language, configuration);
                }
            }

            if (!cachedLocales.containsKey(defaultLanguage)) {
                throw new RuntimeException("Default configuration not loaded! Default language: " + defaultLanguage.key().code());
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void extractLanguages() throws Exception {
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

    public DefaultConfiguration<?> configurationFor(@NotNull P player) {
        var locale = localeFor(player);
        var localeFormat = locale.getLanguage() + "_" + locale.getCountry().toLowerCase(Locale.ROOT);

        var language = Language.fromKey(LanguageKey.of(localeFormat));
        return language.isPresent() ? cachedLocales.get(language.get()) : cachedLocales.get(defaultLanguage);
    }

    public abstract Locale localeFor(@NotNull P player);

    public abstract Translation translationFor(@NotNull P player, @NotNull TranslationKey key);
}
