package noelle.features.languages.velocity;

import com.velocitypowered.api.proxy.Player;

import noelle.features.languages.common.AbstractLanguages;
import noelle.features.languages.common.backend.BackendType;
import noelle.features.languages.common.language.Language;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

public class VelocityLanguages extends AbstractLanguages<Player> {

    protected VelocityLanguages(BackendType type, Path rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        super(type, rootPath, defaultLanguage, target, anyResource);
    }

    public static @NotNull VelocityLanguages init(BackendType type, @NotNull Path rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        var languages = new VelocityLanguages(type, rootPath, defaultLanguage, target, anyResource);
        languages.init();

        return languages;
    }

    public static @NotNull VelocityLanguages init(BackendType type, @NotNull File rootPath, Language defaultLanguage, Class<?> target, String anyResource) {
        return init(type, rootPath.toPath(), defaultLanguage, target, anyResource);
    }

    @Override
    public Locale localeFor(@NotNull Player player) {
        return player.getPlayerSettings().getLocale();
    }
}
