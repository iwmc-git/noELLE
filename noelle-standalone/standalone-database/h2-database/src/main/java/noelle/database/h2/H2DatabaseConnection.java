package noelle.database.h2;

import noelle.database.DefaultConnection;
import noelle.database.credentials.Credentials;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Main of H2 connection.
 */
public interface H2DatabaseConnection {

    /**
     * Builds a new database connection.
     *
     * @param credentials database credentials.
     * @param path root path.
     *
     * @return a new database connection.
     */
    @Contract("_, _ -> new")
    static @NotNull DefaultConnection newConnection(@NotNull Credentials credentials, @NotNull Path path) {
        return new H2DatabaseConnectionImpl(credentials, path);
    }

}
