package noelle.database;

import noelle.database.utils.Wrap;

import java.io.Closeable;
import java.sql.PreparedStatement;

/**
 * Represents an object holding an instance of {@link PreparedStatement}.
 *
 * @param <S> the type of the statement
 */
public interface StatementHolder<S extends PreparedStatement> extends Closeable {

    /**
     * Returns the underlying statement handled by this object.
     *
     * @return the statement
     */
    S statement();

    /**
     * Executes the statement held by this object.
     *
     * @return {@code true} if the first result is a {@link java.sql.ResultSet}
     *         object; {@code false} if the first result is an update count or
     *         there is no result
     * @see PreparedStatement#execute()
     * @see StatementHolder#statement()
     */
    default boolean execute() {
        return Wrap.get(() -> {
            var execute = statement().execute();
            statement().closeOnCompletion();

            return execute;
        });
    }

    /**
     * Closes the statement held by this object.
     */
    default void close() {
        Wrap.execute(statement()::close);
    }
}
