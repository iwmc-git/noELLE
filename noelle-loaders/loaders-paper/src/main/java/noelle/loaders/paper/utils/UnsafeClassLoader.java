package noelle.loaders.paper.utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;

public class UnsafeClassLoader {
    private static final sun.misc.Unsafe UNSAFE;

    static {
        sun.misc.Unsafe unsafe;
        try {
            var unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Throwable t) {
            unsafe = null;
        }

        UNSAFE = unsafe;
    }

    private final Collection<URL> unopenedURLs;
    private final Collection<URL> pathURLs;

    @SuppressWarnings("unchecked")
    public UnsafeClassLoader(URLClassLoader classLoader) {
        Collection<URL> unopenedURLs;
        Collection<URL> pathURLs;

        try {
            Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
            unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
            pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
        } catch (Throwable e) {
            unopenedURLs = null;
            pathURLs = null;
        }

        this.unopenedURLs = unopenedURLs;
        this.pathURLs = pathURLs;
    }

    private static Object fetchField(final @NotNull Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
        var field = clazz.getDeclaredField(name);
        var offset = UNSAFE.objectFieldOffset(field);

        return UNSAFE.getObject(object, offset);
    }

    private void addURL(@NonNull URL url) {
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
