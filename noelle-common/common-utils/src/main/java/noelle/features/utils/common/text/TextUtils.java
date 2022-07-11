package noelle.features.utils.common.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.jetbrains.annotations.NotNull;

public class TextUtils {
    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static @NotNull Component toComponent(@NotNull String content) {
        return MINI_MESSAGE.deserialize(colorize(content));
    }

    public static @NotNull Component toComponent(String content, String @NotNull ... replacements) {
        if (replacements.length != 0) {
            for (var i = 0; i < replacements.length; i += 2) {
                content = content.replace(replacements[i], replacements[i + 1]);
            }
        }

        return toComponent(content);
    }

    public static @NotNull String colorize(@NotNull String content) {
        return content.replace("&", "§");
    }

    public static @NotNull String colorize(@NotNull String content, String @NotNull ... replacements) {
        if (replacements.length != 0) {
            for (var i = 0; i < replacements.length; i += 2) {
                content = content.replace(replacements[i], replacements[i + 1]);
            }
        }

        return colorize(content);
    }

    public static @NotNull Component parseMotd(int onlinePlayers, int maxPlayers, String lineOne, String lineTwo) {
        var concatedLines = lineOne + "\n" + lineTwo;
        return toComponent(concatedLines,
                "%online-players%", String.valueOf(onlinePlayers),
                "%max-players%", String.valueOf(maxPlayers)
        );
    }
}