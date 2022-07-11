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
    @NotNull UnsafeKeyed unsafeKeyed();

    /**
     * Unsafe getters and setters.
     */
    interface UnsafeKeyed {

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
