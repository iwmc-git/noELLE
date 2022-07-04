package noelle.libraries.api;

public final class LibrariesProvider {
    private static LibrariesAPI libraries;

    public static LibrariesAPI libraries() {
        if (libraries == null) {
            throw new NullPointerException("Libraries not initialized!");
        }

        return libraries;
    }

    public static void register(LibrariesAPI libraries) {
        LibrariesProvider.libraries = libraries;
    }

    public static void unregister() {
        LibrariesProvider.libraries = null;
    }
}