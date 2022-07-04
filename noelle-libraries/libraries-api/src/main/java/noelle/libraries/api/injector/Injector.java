package noelle.libraries.api.injector;

import java.nio.file.Path;

public interface Injector {
    void addToClasspath(Path path);
}
