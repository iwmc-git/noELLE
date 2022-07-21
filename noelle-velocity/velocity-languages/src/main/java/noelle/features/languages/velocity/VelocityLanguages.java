package noelle.features.languages.velocity;

import com.velocitypowered.api.proxy.Player;

import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.LanguagedPlugin;
import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.translation.Translation;
import noelle.features.languages.velocity.translation.VelocityTranslation;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class VelocityLanguages extends AbstractLanguages<Player> {

    protected VelocityLanguages(LanguagedPlugin plugin) {
        super(plugin);
    }

    @Contract("_ -> new")
    public static @NotNull VelocityLanguages init(LanguagedPlugin plugin) {
        return new VelocityLanguages(plugin);
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
