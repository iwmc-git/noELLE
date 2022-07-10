package noelle.features.languages.common.key;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record TranslationKey(String key) {

    @Contract("_ -> new")
    public static @NotNull TranslationKey of(String key) {
        return new TranslationKey(key);
    }

}