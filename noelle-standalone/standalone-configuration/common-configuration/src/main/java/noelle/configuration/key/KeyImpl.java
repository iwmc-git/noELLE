package noelle.configuration.key;

import org.jetbrains.annotations.NotNull;

record KeyImpl(String key) implements Key {
    private final static String MODIFIER = "\\.";

    @Override
    public @NotNull String key() {
        return key;
    }

    @Override
    public @NotNull String[] arrayKey() {
        return key.split(MODIFIER);
    }
}
