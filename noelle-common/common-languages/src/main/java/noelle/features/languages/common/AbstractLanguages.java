package noelle.features.languages.common;

import noelle.configuration.DefaultConfiguration;
import noelle.configuration.hocon.HoconLoader;
import noelle.configuration.yaml.YamlLoader;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.service.BackendType;
import noelle.features.languages.common.service.LanguageService;
import noelle.features.languages.common.translation.Translation;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLanguages<P> implements Closeable {
    private final Map<Language, DefaultConfiguration<CommentedConfigurationNode>> cachedLanguages;

    private final LanguageService service;

    private final Path languagesFolder;
    private final Language defaultLanguage;

    private final File fallback;
    private final BackendType backendType;

    protected AbstractLanguages(@NotNull Path pluginFolder, Language defaultLanguage, BackendType backendType) {
        this.defaultLanguage = defaultLanguage;
        this.backendType = backendType;
        this.cachedLanguages = new HashMap<>();

        this.languagesFolder = pluginFolder.resolve("lang");

        try {
            if (Files.notExists(languagesFolder)) {
                Files.createDirectory(languagesFolder);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        var languageFileFormat = defaultLanguage.key().code() + backendType.format();

        this.fallback = new File(languagesFolder.toFile(), languageFileFormat);
        this.service = new LanguageService(this);

        for (var language : Language.values()) {
            DefaultConfiguration<CommentedConfigurationNode> configuration = null;
            var file = new File(languagesFolder.toFile(), languageFileFormat);

            switch (backendType) {
                case HOCON -> configuration = HoconLoader.loader(file.toPath()).configuration();
                case YAML -> configuration = YamlLoader.loader(file.toPath()).configuration();
            }

            cachedLanguages.put(language, configuration);
        }
    }

    @Override
    public void close() throws IOException {
        service.shutdown();
    }

    public Map<Language, DefaultConfiguration<CommentedConfigurationNode>> cachedLanguages() {
        return cachedLanguages;
    }

    public Language defaultLanguage() {
        return defaultLanguage;
    }

    public File fallback() {
        return fallback;
    }

    public Path languagesFolder() {
        return languagesFolder;
    }

    public BackendType backendType() {
        return backendType;
    }

    public abstract String localeFor(P player);

    public abstract Translation translationFor(P player, TranslationKey key);

    public abstract Language languageFor(P player);

    public abstract DefaultConfiguration<CommentedConfigurationNode> configurationFor(P player);
}
