package noelle.utils.unsafe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;

public final class UnsafeLoaderAccess {
    private static final sun.misc.Unsafe UNSAFE;

    static {
        sun.misc.Unsafe unsafe;

        try {
            var unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");

            unsafeField.setAccessible(true);
            unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Throwable throwable) {
            throw new RuntimeException("Unsafe class not found!");
        }

        UNSAFE = unsafe;
    }

    private final Collection<URL> unopenedURLs;
    private final Collection<URL> pathURLs;

    @SuppressWarnings("unchecked")
    public UnsafeLoaderAccess(URLClassLoader classLoader) {
        Collection<URL> unopenedURLs;
        Collection<URL> pathURLs;

        try {
            var ucp = fetchField(URLClassLoader.class, classLoader, "ucp");

            unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
            pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
        } catch (Throwable e) {
            unopenedURLs = null;
            pathURLs = null;
        }

        this.unopenedURLs = unopenedURLs;
        this.pathURLs = pathURLs;
    }

    @Contract("null -> fail")
    public static @NotNull UnsafeLoaderAccess createLoader(ClassLoader classLoader) {
        if (classLoader instanceof URLClassLoader urlClassLoader) {
            return new UnsafeLoaderAccess(urlClassLoader);
        }

        throw new RuntimeException("Provided classloader is not URLClassLoader!");
    }

    private Object fetchField(final @NotNull Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
        var field = clazz.getDeclaredField(name);
        var offset = UNSAFE.objectFieldOffset(field);

        return UNSAFE.getObject(object, offset);
    }

    private void addURL(@NotNull URL url) {
        if (this.unopenedURLs == null || this.pathURLs == null) {
            throw new NullPointerException("unopenedURLs or pathURLs");
        }

        this.unopenedURLs.add(url);
        this.pathURLs.add(url);
    }

    public void addPath(Path path) {
        try {
            addURL(path.toUri().toURL());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}