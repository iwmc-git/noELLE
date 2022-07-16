package noelle.database.utils;

import noelle.database.exceptions.UncheckedSQLException;
import noelle.database.functions.SqlAction;
import noelle.database.functions.SqlSupplier;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * Utility to execute code that might throw a checked {@link SQLException} so that
 * it throws {@link UncheckedSQLException} instead.
 */
public final class Wrap {

    private Wrap() { }

    /**
     * Executes a possibly throwing code, wrapping any {@code SQLException} into
     * a {@code UncheckedSqlException}.
     *
     * @param action the action to execute
     * @throws UncheckedSQLException if an {@code SQLException} was thrown
     */
    public static void execute(@NotNull SqlAction action) throws UncheckedSQLException {
        try {
            action.execute();
        } catch (SQLException sqlException) {
            throw new UncheckedSQLException(sqlException);
        }
    }

    /**
     * Executes a possibly throwing code and returns the result, wrapping any {@code
     * SQLException} into a {@code UncheckedSqlException}.
     *
     * @param supplier the action to execute
     * @param <T> the type of the object returned
     * @return the object returned by the supplier
     * @throws UncheckedSQLException if an {@code SQLException} was thrown
     */
    public static <T> T get(@NotNull SqlSupplier<T> supplier) throws UncheckedSQLException {
        try {
            return supplier.get();
        } catch (SQLException sqlException) {
            throw new UncheckedSQLException(sqlException);
        }
    }
}
