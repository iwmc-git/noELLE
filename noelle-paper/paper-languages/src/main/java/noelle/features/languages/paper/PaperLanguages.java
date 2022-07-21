package noelle.features.languages.paper;

import noelle.features.languages.common.key.TranslationKey;
import noelle.features.languages.common.translation.Translation;
import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.LanguagedPlugin;
import noelle.features.languages.paper.translation.PaperTranslation;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PaperLanguages extends AbstractLanguages<Player> {

    protected PaperLanguages(LanguagedPlugin plugin) {
        super(plugin);
    }

    @Contract("_ -> new")
    public static @NotNull PaperLanguages init(LanguagedPlugin plugin) {
        return new PaperLanguages(plugin);
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
