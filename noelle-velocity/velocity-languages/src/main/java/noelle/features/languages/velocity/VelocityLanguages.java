package noelle.features.languages.velocity;

import com.velocitypowered.api.proxy.Player;

import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.enums.Language;
import noelle.features.languages.common.enums.LanguageBackend;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.translation.Translation;
import noelle.features.languages.velocity.translation.VelocityTranslation;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Locale;

public class VelocityLanguages extends AbstractLanguages<Player> {

    protected VelocityLanguages(Path rootDirectory, Language defaultLanguage, LanguageBackend backend, Class<?> targetClass, String includedResource) {
        super(rootDirectory, defaultLanguage, backend, targetClass, includedResource);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull VelocityLanguages init(Path rootDirectory, Class<?> targetClass, Language defaultLanguage, LanguageBackend backend) {
        return new VelocityLanguages(rootDirectory, defaultLanguage, backend, targetClass, "velocity-plugin.json");
    }

    @Contract("_, _, _ -> new")
    public static @NotNull VelocityLanguages init(Path rootDirectory, Class<?> targetClass, Language defaultLanguage) {
        return VelocityLanguages.init(rootDirectory, targetClass, defaultLanguage, LanguageBackend.YAML);
    }

    @Contract("_, _ -> new")
    public static @NotNull VelocityLanguages init(Path rootDirectory, Class<?> targetClass) {
        return VelocityLanguages.init(rootDirectory, targetClass, Language.ENGLISH_AMERICAN);
    }

    @Override
    public Locale localeFor(@NotNull Player player) {
        return player.getPlayerSettings().getLocale();
    }

    @Override
    public Translation translationFor(@NotNull Player player, @NotNull TranslationKey key) {
        var configurationFor = configurationFor(player);
        var configKey = key.key();

        return new VelocityTranslation(configurationFor.bump(configKey), player);
    }
}
