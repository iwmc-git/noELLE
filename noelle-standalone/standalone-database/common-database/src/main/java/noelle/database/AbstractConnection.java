package noelle.database;

import be.bendem.sqlstreams.SqlStream;
import com.zaxxer.hikari.HikariDataSource;

import noelle.database.credentials.Credentials;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractConnection implements DefaultConnection {
    protected SqlStream sqlStream;
    protected HikariDataSource dataSource;

    public abstract void init(@NotNull Credentials credentials) throws Exception;

    @Override
    public HikariDataSource dataSource() {
        return dataSource;
    }

    @Override
    public SqlStream sqlStream() {
        return sqlStream;
    }

    @Override
    public Connection connection() throws SQLException {
        return Objects.requireNonNull(dataSource.getConnection(), "connection is null");
    }

    @Override
    public void execute(String statement, Consumer<PreparedStatement> preparer) {
        try (var connection = connection(); var preparedStatement = connection.prepareStatement(statement)) {
            preparer.accept(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public <R> Optional<R> query(String query, Consumer<PreparedStatement> preparer, Function<ResultSet, R> handler) {
        try (var connection = connection(); var preparedStatement = connection.prepareStatement(query)) {
            preparer.accept(preparedStatement);

            try (var resultSet = preparedStatement.executeQuery()) {
                return Optional.ofNullable(handler.apply(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
        sqlStream.close();
    }
}
