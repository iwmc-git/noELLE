package noelle.features.messages.common.key;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record MessageKey(String key) {

    @Contract("_ -> new")
    public static @NotNull MessageKey of(String key) {
        return new MessageKey(key);
    }

}
