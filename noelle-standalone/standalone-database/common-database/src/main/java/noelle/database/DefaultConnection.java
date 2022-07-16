package noelle.database;

import noelle.database.functions.SqlFunction;
import noelle.database.utils.binder.PreparedStatementBinderByIndex;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Main interface of database connection.
 */
public interface DefaultConnection extends Closeable {

    /**
     * Registers new binder.
     *
     * @param clazz binder class.
     * @param preparedStatementBinderByIndex statement binder.
     * @param <T> expected binder type.
     *
     * @return this instance with mapped binding.
     */
    <T> DefaultConnection registerCustomBinding(Class<T> clazz, PreparedStatementBinderByIndex<T> preparedStatementBinderByIndex);

    /**
     * Manually prepares a query from a {@link Connection}.
     *
     * @param preparer the code creating a {@link PreparedStatement} from a
     *                 {@link Connection}
     * @return an object to parametrize the statement and map the query result
     */
    Query query(SqlFunction<Connection, PreparedStatement> preparer);

    /**
     * Prepares a query to be executed.
     * <p>
     * Note that the query is not actually executed until a mapping method
     * of {@link Query} is called.
     *
     * @param sql the sql query
     * @return an object to parametrize the statement and map the query result
     */
    default Query query(String sql) {
        return query(conn -> conn.prepareStatement(sql));
    }

    /**
     * Manually prepares a DML query from a {@link Connection}.
     *
     * @param preparer the code creating a {@link PreparedStatement} from a
     *                 {@link Connection}
     * @return an object to parametrize and execute the DML statement
     */
    Update update(SqlFunction<Connection, PreparedStatement> preparer);

    /**
     * Prepares a DML sql statement.
     * <p>
     * Not that the query is not actually executed until you invoke a
     * method from {@link Update}.
     *
     * @param sql the sql query
     * @return an object to parametrize the statement and retrieve the number
     *         of rows affected by this query
     */
    default Update update(String sql) {
        return update(conn -> conn.prepareStatement(sql));
    }

    /**
     * Prepares a DML statement to provide it multiple batches of parameters.
     * <p>
     * Note that the query is not actually executed until you invoke a
     * count method from {@link BatchUpdate}.
     *
     * @param sql the sql query
     * @return an object to parametrize the statement and retrieve counts
     *         of affected rows
     */
    BatchUpdate batchUpdate(String sql);

    /**
     * Prepares a query.
     * <p>
     * Note that this method is not executed until you call {@link Execute#execute()}.
     *
     * @param sql the sql query
     * @return an object to parametrize the statement and execute the query
     */
    Execute<PreparedStatement> execute(String sql);

    /**
     * Prepares a call and provides it the given parameters.
     * <p>
     * Note that this method is not executed until you call {@link Execute#execute()}.
     *
     * @param sql the sql query
     * @return an object to parametrize the statement and execute the query
     * @see Connection#prepareCall(String)
     */
    Execute<CallableStatement> call(String sql);

    /**
     * Shortcut for {@link #query(String) query(sql).with(parameters).first(mapping)}.
     *
     * @param sql the sql query
     * @param mapping a function to map each row to an object
     * @param parameters parameters to apply in order to the provided query
     * @param <R> the type of the elements of the returned stream
     * @return a stream of elements mapped from the result set
     * @see #query(String)
     * @see Query#first(SqlFunction)
     */
    default <R> Optional<R> first(String sql, SqlFunction<ResultSet, R> mapping, Object... parameters) {
        try (var query = query(sql).with(parameters)) {
            return query.first(mapping);
        }
    }

    /**
     * Shortcut for {@link #execute(String) execute(sql).with(parameters).execute()}.
     *
     * @param sql the sql query
     * @param parameters parameters to apply in order to the provided query
     */
    default void exec(String sql, Object... parameters) {
        try (var execute = execute(sql).with(parameters)) {
            execute.execute();
        }
    }

    /**
     * Closes the underlying {@link DataSource}.
     */
    void close();
}