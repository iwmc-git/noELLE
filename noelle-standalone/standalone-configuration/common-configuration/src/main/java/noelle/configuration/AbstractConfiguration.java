package noelle.configuration;

import io.leangen.geantyref.TypeToken;
import noelle.configuration.key.Key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConfiguration<N extends ScopedConfigurationNode<N>> implements DefaultConfiguration<N> {
    private final UnsafeImpl unsafeInstance = new UnsafeImpl();
    private final DefaultImpl defaultInstance = new DefaultImpl();

    private final N node;

    public AbstractConfiguration(N node) {
        this.node = node;
    }

    @Override
    public abstract @NotNull DefaultConfiguration<N> bump(@NotNull String key);

    private class UnsafeImpl implements Unsafe {

        @Override
        public <V> @Nullable V value(@NotNull String key, @NotNull TypeToken<V> token) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.get(token);
        }

        @Override
        public @Nullable <V> List<V> list(@NotNull String key, @NotNull TypeToken<V> token) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getList(token);
        }

        @Override
        public void newValue(@NotNull String key, Object value) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            prepared.set(value);
        }

        @Override
        public <T> void newValue(@NotNull String key, @NotNull TypeToken<T> token, T value) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            prepared.set(token, value);
        }

        @Override
        public <L> void newList(@NotNull String key, @NotNull TypeToken<L> token, List<L> value) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            prepared.setList(token, value);
        }

        @Override
        public <C> @Nullable C classOf(@NotNull String key, @NotNull TypeToken<C> token) throws SerializationException {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.get(token);
        }
    }

    private class DefaultImpl implements Default {

        @Override
        public String stringValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getString();
        }

        @Override
        public String stringValue(@NotNull String key, String def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getString(def);
        }

        @Override
        public int intValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getInt();
        }

        @Override
        public int intValue(@NotNull String key, int def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getInt(def);
        }

        @Override
        public long longValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getLong();
        }

        @Override
        public long longValue(@NotNull String key, long def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getLong(def);
        }

        @Override
        public float floatValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getFloat();
        }

        @Override
        public float floatValue(@NotNull String key, float def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getFloat(def);
        }

        @Override
        public double doubleValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getDouble();
        }

        @Override
        public double doubleValue(@NotNull String key, double def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getDouble(def);
        }

        @Override
        public boolean booleanValue(@NotNull String key) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getBoolean();
        }

        @Override
        public boolean booleanValue(@NotNull String key, boolean def) {
            var prepared = AbstractConfiguration.this.prepareNode(key);
            return prepared.getBoolean(def);
        }

        @Override
        public byte byteValue(@NotNull String key) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return (byte) prepared.get(Byte.class, 0);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public byte byteValue(@NotNull String key, byte def) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.get(Byte.class, def);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public List<String> stringList(@NotNull String key) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.getList(String.class);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public List<String> stringList(@NotNull String key, List<String> def) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.getList(String.class, def);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public List<Integer> integerList(@NotNull String key) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.getList(Integer.class);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public List<Integer> integerList(@NotNull String key, List<Integer> def) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.getList(Integer.class, def);
            } catch (SerializationException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public @NotNull Unsafe unsafe() {
        return unsafeInstance;
    }

    @Override
    public @NotNull Default defaults() {
        return defaultInstance;
    }

    @Override
    public boolean isVirtual() {
        return node.virtual();
    }

    @Override
    public Collection<Key> keys() {
        return node.childrenMap().keySet().stream().map(Key::keyOf).collect(Collectors.toList());
    }

    @Override
    public <V> V value(Class<V> clazz) {
        try {
            return node.get(clazz);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void newValue(Class<V> type, V value) {
        try {
            node.set(type, value);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isList() {
        return node.isList();
    }

    @Override
    public boolean isMap() {
        return node.isMap();
    }

    @Override
    public boolean isNull() {
        return node.isNull();
    }

    @Override
    public @NotNull N currentNode() {
        return node;
    }

    protected N prepareNode(String key) {
        var buildedKey = Key.keyOf(key);
        var arrayKey = (Object) buildedKey.arrayKey();

        return node.node(arrayKey);
    }
}
