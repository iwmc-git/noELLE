package noelle.libraries.api.objects;

import org.jetbrains.annotations.NotNull;

public interface Repository {
    String url();

    static @NotNull Repository of(String url) {
        return new RepositoryImpl(url);
    }
}
