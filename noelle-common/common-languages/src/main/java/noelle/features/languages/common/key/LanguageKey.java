package noelle.features.languages.common.key;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record LanguageKey(String code) {

    @Contract("_ -> new")
    public static @NotNull LanguageKey of(String code) {
        return new LanguageKey(code);
    }

}
