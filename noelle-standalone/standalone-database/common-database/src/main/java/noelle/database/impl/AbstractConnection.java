package noelle.database.impl;

import noelle.database.*;
import noelle.database.exceptions.UncheckedSQLException;
import noelle.database.functions.SqlBiFunction;
import noelle.database.functions.SqlFunction;
import noelle.database.impl.batch.BatchUpdateImpl;
import noelle.database.impl.bindings.SqlBindings;
import noelle.database.impl.execute.ExecuteImpl;
import noelle.database.impl.query.QueryImpl;
import noelle.database.impl.update.UpdateImpl;
import noelle.database.utils.Creator;
import noelle.database.utils.Wrap;
import noelle.database.utils.binder.PreparedStatementBinderByIndex;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

public abstract class AbstractConnection implements DefaultConnection {
    private final DataSource dataSource;
    private final SqlBindings bindings;

    public AbstractConnection(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.bindings = new SqlBindings();
    }

    protected Connection connection() {
        return Wrap.get(dataSource::getConnection);
    }

    protected boolean closeConnectionAfterAction() {
        return true;
    }

    @Override
    public <T> AbstractConnection registerCustomBinding(Class<T> clazz, PreparedStatementBinderByIndex<T> preparedStatementBinderByIndex) {
        bindings.addMapping(clazz, null, null, preparedStatementBinderByIndex);
        return this;
    }

    @Override
    public Query query(SqlFunction<Connection, PreparedStatement> preparer) {
        return prepare(QueryImpl::new, preparer);
    }

    @Override
    public Update update(SqlFunction<Connection, PreparedStatement> preparer) {
        return prepare(UpdateImpl::new, preparer);
    }

    @Override
    public BatchUpdate batchUpdate(String sql) {
        return prepare(sql, BatchUpdateImpl::new, Connection::prepareStatement);
    }

    @Override
    public Execute<PreparedStatement> execute(String sql) {
        return prepare(sql, ExecuteImpl::new, Connection::prepareStatement);
    }

    @Override
    public Execute<CallableStatement> call(String sql) {
        return prepare(sql, ExecuteImpl::new, Connection::prepareCall);
    }

    @Override
    public void close() {
        if (dataSource instanceof AutoCloseable autoCloseable) {
            try {
                autoCloseable.close();
            } catch (SQLException e) {
                throw new UncheckedSQLException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T, S extends Statement> T prepare(@NotNull Creator<T, S> creator, SqlFunction<Connection, S> statementCreator) {
        var connection = connection();
        return creator.create(this, connection, Wrap.get(() -> statementCreator.apply(connection)), closeConnectionAfterAction());
    }

    private <T, S extends Statement> T prepare(String sql, @NotNull Creator<T, S> creator, SqlBiFunction<Connection, String, S> statementCreator) {
        var connection = connection();
        return creator.create(this, connection, Wrap.get(() -> statementCreator.apply(connection, sql)), closeConnectionAfterAction());
    }

    public SqlBindings bindings() {
        return bindings;
    }
}