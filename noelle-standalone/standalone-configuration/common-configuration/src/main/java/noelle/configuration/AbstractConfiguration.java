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
    private final N node;

    private UnsafeKeyedKeyedConfiguration unsafeInstance;

    public AbstractConfiguration(N node) {
        this.node = node;
    }

    @Override
    public abstract @NotNull DefaultConfiguration<N> bump(@NotNull String key);

    private class UnsafeKeyedKeyedConfiguration implements UnsafeKeyed {

        @Override
        public <V> @Nullable V value(@NotNull String key, @NotNull TypeToken<V> token) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.get(token);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public @Nullable <V> List<V> list(@NotNull String key, @NotNull TypeToken<V> token) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.getList(token);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public void newValue(@NotNull String key, Object value) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                prepared.set(value);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public <T> void newValue(@NotNull String key, @NotNull TypeToken<T> token, T value) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                prepared.set(token, value);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public <L> void newList(@NotNull String key, @NotNull TypeToken<L> token, List<L> value) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                prepared.setList(token, value);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public <C> @Nullable C classOf(@NotNull String key, @NotNull TypeToken<C> token) {
            try {
                var prepared = AbstractConfiguration.this.prepareNode(key);
                return prepared.get(token);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public @NotNull UnsafeKeyed unsafeKeyed() {
        return new UnsafeKeyedKeyedConfiguration();
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
    public <V> List<V> list(Class<V> clazz) {
        try {
            return node.getList(clazz);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void newList(Class<V> type, List<V> value) {
        try {
            node.setList(type, value);
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
        var arrayKey = (Object[]) buildedKey.arrayKey();

        return node.node(arrayKey);
    }
}
