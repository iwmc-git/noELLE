package noelle.database.mariadb;

import noelle.database.DefaultConnection;
import noelle.database.credentials.Credentials;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Main of MariaDB connection.
 */
public interface MariaDBDatabaseConnection {

    /**
     * Builds a new database connection.
     *
     * @param credentials database credentials.
     *
     * @return a new database connection.
     */
    @Contract("_ -> new")
    static @NotNull DefaultConnection newConnection(Credentials credentials) {
        return new MariaDBDatabaseConnectionImpl(credentials);
    }

}
