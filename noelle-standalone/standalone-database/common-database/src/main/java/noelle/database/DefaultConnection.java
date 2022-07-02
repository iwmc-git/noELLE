package noelle.database;

import be.bendem.sqlstreams.SqlStream;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DefaultConnection extends AutoCloseable {

    /**
     * Returns loaded databsource instance for this ${@link DefaultConnection}.
     *
     * @return a datasource.
     */
    HikariDataSource dataSource();

    /**
     * Gets a {@link SqlStream} instance for this {@link DefaultConnection}.
     *
     * @return a ${@link SqlStream}.
     */
    SqlStream sqlStream();

    /**
     * Gets a connection from the datasource.
     *
     * @return a connection.
     */
    Connection connection() throws SQLException;

    /**
     * Executes a database statement with preparation.
     *
     * @param statement the statement to be executed.
     * @param preparer the preparation used for this statement.
     */
    void execute(String statement, Consumer<PreparedStatement> preparer);

    /**
     * Executes a database statement with no preparation.
     *
     * @param statement the statement to be executed.
     */
    default void execute(String statement) {
        execute(statement, (stmt) -> {});
    }

    /**
     * Executes a database query with preparation.
     *
     * @param query the query to be executed.
     * @param preparer the preparation used for this statement.
     * @param handler the handler for the data returned by the query.
     * @param <R> the returned type.
     *
     * @return the results of the database query.
     */
    <R> Optional<R> query(String query, Consumer<PreparedStatement> preparer, Function<ResultSet, R> handler);

    /**
     * Executes a database query with no preparation.
     *
     * @param query the query to be executed.
     * @param handler the handler for the data returned by the query.
     * @param <R> the returned type.
     *
     * @return the results of the database query.
     */
    default <R> Optional<R> query(String query, Function<ResultSet, R> handler) {
        return query(query, (stmt) -> {}, handler);
    }

    /**
     * Closes a connection.
     *
     * @throws Exception if something went wrong. :p
     */
    @Override
    void close() throws Exception;

    /**
     * Closes the connection silently, without errors.
     */
    default void closeSilent() {
        try {
            close();
        } catch (Exception exception) {
            // ignore
        }
    }
}
