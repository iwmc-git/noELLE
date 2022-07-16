package noelle.database.functions;

import java.sql.SQLException;

/**
 * Represents a supplier of data.
 *
 * @param <T> the type of the supplied data
 */
@FunctionalInterface
public interface SqlSupplier<T> {

    /**
     * Gets a result.
     *
     * @return the result
     * @throws SQLException generally rethrown as {@link noelle.database.exceptions.UncheckedSQLException}
     */
    T get() throws SQLException;
}
