package noelle.features.languages.velocity;

import com.velocitypowered.api.proxy.Player;

import noelle.configuration.DefaultConfiguration;
import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.Language;
import noelle.features.languages.common.key.LanguageKey;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.service.BackendType;
import noelle.features.languages.common.translation.Translation;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;
import java.nio.file.Path;

public class VelocityLanguages extends AbstractLanguages<Player> {
    private final Language defaultLanguage;

    public VelocityLanguages(Path pluginFolder, Language defaultLanguage, BackendType backendType) {
        super(pluginFolder, defaultLanguage, backendType);
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public String localeFor(@NotNull Player player) {
        return player.getPlayerSettings().getLocale().getDisplayCountry().toLowerCase();
    }

    @Override
    public Translation translationFor(Player player, @NotNull TranslationKey key) {
        var languageFileFormat = defaultLanguage.key().code() + backendType().format();

        var lang = fallback().getAbsoluteFile().getParentFile().toString();
        var targetLanguage = languageFor(player);
        var file = new File(lang, languageFileFormat);
        var config = file.exists() ? cachedLanguages().get(targetLanguage) : cachedLanguages().get(defaultLanguage);

        if (config.bump(key.key()) != null) {
            var nextNode = config.bump(key.key());
            
            if (nextNode.isList()) {
                return Translation.of(targetLanguage, config.defaults().stringList(key.key()));
            } else {
                return Translation.of(targetLanguage, config.defaults().stringValue(key.key()));
            }
        } else {
            return Translation.of(Language.ENGLISH, "TRANSLATION " + key.key() + " NOT FOUND!");
        }
    }

    @Override
    public Language languageFor(Player player) {
        var locale = localeFor(player);
        var key = LanguageKey.of(locale);

        return Language.fromKey(key).orElse(defaultLanguage);
    }

    @Override
    public DefaultConfiguration<CommentedConfigurationNode> configurationFor(Player player) {
        var key = Language.fromKey(LanguageKey.of(localeFor(player)));
        return key.map(language -> cachedLanguages().get(language)).orElse(cachedLanguages().get(defaultLanguage));
    }
}
