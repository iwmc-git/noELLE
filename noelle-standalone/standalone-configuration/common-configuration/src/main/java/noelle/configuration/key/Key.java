package noelle.configuration.key;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Key {

    /**
     * Returns configuration key.
     */
    @NotNull String key();

    /**
     * Returns arrayed configuration key.
     */
    @NotNull String[] arrayKey();

    @Contract("_ -> new")
    static @NotNull Key keyOf(Object rawKey) {
        return new KeyImpl((String) rawKey);
    }
}
