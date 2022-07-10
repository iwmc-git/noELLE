package noelle.configuration;

import io.leangen.geantyref.TypeToken;
import noelle.configuration.key.Key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.List;

public interface DefaultConfiguration<N extends ConfigurationNode> {

    /**
     * Bumps to another section by key.
     *
     * @param key configuration key.
     * @return selected configuration section.
     */
    DefaultConfiguration<N> bump(@NotNull String key);

    /**
     * Bumps to another section by key.
     *
     * @param key configuration key.
     * @return selected configuration section.
     */
    default @NotNull DefaultConfiguration<N> bump(@NotNull Key key) {
        return bump(key.key());
    }

    /**
     * Returns unsafe getters and setters.
     */
    @NotNull Unsafe unsafe();

    /**
     * Returns default getters and setters.
     */
    @NotNull Default defaults();

    /**
     * Unsafe getters and setters.
     */
    interface Unsafe {

        /**
         * Returns value provided by key.
         *
         * @param key configuration key.
         * @param token class type token.
         *
         * @return value provided by key.
         */
        <V> @Nullable V value(@NotNull String key, @NotNull TypeToken<V> token) throws SerializationException;

        /**
         * Returns value provided by key.
         *
         * @param key configuration key.
         * @param token class type token.
         *
         * @return value provided by key.
         */
        default <V> @Nullable V value(@NotNull Key key, @NotNull TypeToken<V> token) throws SerializationException {
            return value(key.key(), token);
        }

        /**
         * Returns value provided by key.
         *
         * @param key configuration key.
         * @param token class type.
         *
         * @return value provided by key.
         */
        default <V> @Nullable V value(@NotNull String key, @NotNull Class<V> token) throws SerializationException {
            return value(key, TypeToken.get(token));
        }

        /**
         * Returns value provided by key.
         *
         * @param key configuration key.
         * @param token class type.
         *
         * @return value provided by key.
         */
        default <V> @Nullable V value(@NotNull Key key, @NotNull Class<V> token) throws SerializationException {
            return value(key, TypeToken.get(token));
        }

        /**
         * Returns list provided by key.
         *
         * @param key configuration key.
         * @param token class type token.
         *
         * @return list provided by key.
         */
        <V> @Nullable List<V> list(@NotNull String key, @NotNull TypeToken<V> token) throws SerializationException;

        /**
         * Returns list provided by key.
         *
         * @param key configuration key.
         * @param token class type token.
         *
         * @return list provided by key.
         */
        default <V> @Nullable List<V> list(@NotNull Key key, @NotNull TypeToken<V> token) throws SerializationException {
            return list(key.key(), token);
        }

        /**
         * Returns list provided by key.
         *
         * @param key configuration key.
         * @param token class type.
         *
         * @return list provided by key.
         */
        default <V> @Nullable List<V> list(@NotNull String key, @NotNull Class<V> token) throws SerializationException {
            return list(key, TypeToken.get(token));
        }

        /**
         * Returns list provided by key.
         *
         * @param key configuration key.
         * @param token class type.
         *
         * @return list provided by key.
         */
        default <V> @Nullable List<V> list(@NotNull Key key, @NotNull Class<V> token) throws SerializationException {
            return list(key, TypeToken.get(token));
        }

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param value any value.
         */
        void newValue(@NotNull String key, Object value) throws SerializationException;

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param value any value.
         */
        default void newValue(@NotNull Key key, Object value) throws SerializationException {
            newValue(key.key(), value);
        }

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param token value type.
         * @param value any value.
         */
        <T> void newValue(@NotNull String key, @NotNull TypeToken<T> token, T value) throws SerializationException;

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param token value type.
         * @param value any value.
         */
        default <T> void newValue(@NotNull Key key, @NotNull TypeToken<T> token, T value) throws SerializationException {
            newValue(key.key(), token, value);
        }

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param token value type.
         * @param value any value.
         */
        default <T> void newValue(@NotNull Key key, @NotNull Class<T> token, T value) throws SerializationException {
            newValue(key.key(), TypeToken.get(token), value);
        }

        /**
         * Sets new value provided by key.
         *
         * @param key configuration.
         * @param token value type.
         * @param value any value.
         */
        default <T> void newValue(@NotNull String key, @NotNull Class<T> token, T value) throws SerializationException {
            newValue(key, TypeToken.get(token), value);
        }

        /**
         * Sets new list provided by key.
         *
         * @param key configuration.
         * @param token list type.
         * @param value any list.
         */
        <L> void newList(@NotNull String key, @NotNull TypeToken<L> token, List<L> value) throws SerializationException;

        /**
         * Sets new list provided by key.
         *
         * @param key configuration.
         * @param token list type.
         * @param value any list.
         */
        default <L> void newList(@NotNull Key key, @NotNull TypeToken<L> token, List<L> value) throws SerializationException {
            newList(key.key(), token, value);
        }

        /**
         * Sets new list provided by key.
         *
         * @param key configuration.
         * @param token list type.
         * @param value any list.
         */
        default <L> void newList(@NotNull String key, @NotNull Class<L> token, List<L> value) throws SerializationException {
            newList(key, TypeToken.get(token), value);
        }

        /**
         * Sets new list provided by key.
         *
         * @param key configuration.
         * @param token list type.
         * @param value any list.
         */
        default <L> void newList(@NotNull Key key, @NotNull Class<L> token, List<L> value) throws SerializationException {
            newList(key, TypeToken.get(token), value);
        }

        /**
         * Returns serialized data class of provided token.
         *
         * @param key configuration key.
         * @param token token class for serialize.
         *
         * @return serialized class.
         */
        <C> @Nullable C classOf(@NotNull String key, @NotNull TypeToken<C> token) throws SerializationException;

        /**
         * Returns serialized data class of provided token.
         *
         * @param key configuration key.
         * @param token token class for serialize.
         *
         * @return serialized class.
         */
        default <C> @Nullable C classOf(@NotNull Key key, @NotNull TypeToken<C> token) throws SerializationException {
            return classOf(key.key(), token);
        }

        /**
         * Returns serialized data class of provided token.
         *
         * @param key configuration key.
         * @param token token class for serialize.
         *
         * @return serialized class.
         */
        default <C> @Nullable C classOf(@NotNull String key, @NotNull Class<C> token) throws SerializationException {
            return classOf(key, TypeToken.get(token));
        }

        /**
         * Returns serialized data class of provided token.
         *
         * @param key configuration key.
         * @param token token class for serialize.
         *
         * @return serialized class.
         */
        default <C> @Nullable C classOf(@NotNull Key key, @NotNull Class<C> token) throws SerializationException {
            return classOf(key.key(), token);
        }
    }

    /**
     * Default getters and setters.
     */
    interface Default {

        /**
         * Returns string value provided by key.
         *
         * @param key configuration key.
         */
        String stringValue(@NotNull String key);

        /**
         * Returns string value provided by key.
         *
         * @param key configuration key.
         */
        default String stringValue(@NotNull Key key) {
            return stringValue(key.key());
        }

        /**
         * Returns string value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        String stringValue(@NotNull String key, String def);

        /**
         * Returns string value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default String stringValue(@NotNull Key key, String def) {
            return stringValue(key.key(), def);
        }

        /**
         * Returns int value provided by key.
         *
         * @param key configuration key.
         */
        int intValue(@NotNull String key);

        /**
         * Returns int value provided by key.
         *
         * @param key configuration key.
         */
        default int intValue(@NotNull Key key) {
            return intValue(key.key());
        }

        /**
         * Returns int value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        int intValue(@NotNull String key, int def);

        /**
         * Returns int value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default int intValue(@NotNull Key key, int def) {
            return intValue(key.key(), def);
        }

        /**
         * Returns long value provided by key.
         *
         * @param key configuration key.
         */
        long longValue(@NotNull String key);

        /**
         * Returns int value provided by key.
         *
         * @param key configuration key.
         */
        default long longValue(@NotNull Key key) {
            return longValue(key.key());
        }

        /**
         * Returns int value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        long longValue(@NotNull String key, long def);

        /**
         * Returns int value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default long longValue(@NotNull Key key, long def) {
            return longValue(key.key(), def);
        }

        /**
         * Returns float value provided by key.
         *
         * @param key configuration key.
         */
        float floatValue(@NotNull String key);

        /**
         * Returns float value provided by key.
         *
         * @param key configuration key.
         */
        default float floatValue(@NotNull Key key) {
            return floatValue(key.key());
        }

        /**
         * Returns float value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        float floatValue(@NotNull String key, float def);

        /**
         * Returns float value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default float floatValue(@NotNull Key key, float def) {
            return floatValue(key.key(), def);
        }

        /**
         * Returns double value provided by key.
         *
         * @param key configuration key.
         */
        double doubleValue(@NotNull String key);

        /**
         * Returns double value provided by key.
         *
         * @param key configuration key.
         */
        default double doubleValue(@NotNull Key key) {
            return doubleValue(key.key());
        }

        /**
         * Returns double value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        double doubleValue(@NotNull String key, double def);

        /**
         * Returns double value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default double doubleValue(@NotNull Key key, double def) {
            return doubleValue(key.key(), def);
        }

        /**
         * Returns boolean value provided by key.
         *
         * @param key configuration key.
         */
        boolean booleanValue(@NotNull String key);

        /**
         * Returns boolean value provided by key.
         *
         * @param key configuration key.
         */
        default boolean booleanValue(@NotNull Key key) {
            return booleanValue(key.key());
        }

        /**
         * Returns boolean value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        boolean booleanValue(@NotNull String key, boolean def);

        /**
         * Returns boolean value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default boolean booleanValue(@NotNull Key key, boolean def) {
            return booleanValue(key.key(), def);
        }

        /**
         * Returns byte value provided by key.
         *
         * @param key configuration key.
         */
        byte byteValue(@NotNull String key);

        /**
         * Returns byte value provided by key.
         *
         * @param key configuration key.
         */
        default byte byteValue(@NotNull Key key) {
            return byteValue(key.key());
        }

        /**
         * Returns byte value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        byte byteValue(@NotNull String key, byte def);

        /**
         * Returns byte value provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default byte byteValue(@NotNull Key key, byte def) {
            return byteValue(key.key(), def);
        }

        /**
         * Returns string list provided by key.
         *
         * @param key configuration key.
         */
        List<String> stringList(@NotNull String key);

        /**
         * Returns string list provided by key.
         *
         * @param key configuration key.
         */
        default List<String> stringList(@NotNull Key key) {
            return stringList(key.key());
        }

        /**
         * Returns string list provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        List<String> stringList(@NotNull String key, List<String> def);

        /**
         * Returns string list provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default List<String> stringList(@NotNull Key key, List<String> def) {
            return stringList(key.key(), def);
        }

        /**
         * Returns integer list provided by key.
         *
         * @param key configuration key.
         */
        List<Integer> integerList(@NotNull String key);

        /**
         * Returns integer list provided by key.
         *
         * @param key configuration key.
         */
        default List<Integer> integerList(@NotNull Key key) {
            return integerList(key.key());
        }

        /**
         * Returns integer list provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        List<Integer> integerList(@NotNull String key, List<Integer> def);

        /**
         * Returns integer list provided by key.
         * If value wasn`t find, returned default value.
         *
         * @param key configuration key.
         * @param def default value.
         */
        default List<Integer> integerList(@NotNull Key key, List<Integer> def) {
            return integerList(key.key(), def);
        }
    }

    /**
     * Checks if this section is virtual.
     */
    boolean isVirtual();

    /**
     * Returns all keys for selected configuration.
     */
    Collection<Key> keys();

    /**
     * Returns value in selected node.
     *
     * @param clazz value type.
     * @param <V> expected type.
     *
     * @return the value.
     */
    <V> V value(Class<V> clazz);

    /**
     * Returns list in selected node.
     *
     * @param clazz value type.
     * @param <V> expected type.
     *
     * @return the value.
     */
    <V> List<V> list(Class<V> clazz);

    /**
     * Sets new value in selected node.
     *
     * @param type value type.
     * @param value value.
     * @param <V> expected type.
     */
    <V> void newValue(Class<V> type, V value);

    /**
     * Sets new list in selected node.
     *
     * @param type value type.
     * @param value value.
     * @param <V> expected type.
     */
    <V> void newList(Class<V> type, List<V> value);

    /**
     * Checks the value of the key if it is a list.
     */
    boolean isList();

    /**
     * Checks the value of the key if it is a map.
     */
    boolean isMap();

    /**
     * Checks the value of the key if it is a null.
     */
    boolean isNull();

    /**
     * Returns current node of [ConfigurationNode].
     */
    @NotNull N currentNode();
}
