package noelle.database.impl.query;

import noelle.database.Query;
import noelle.database.functions.SqlFunction;
import noelle.database.impl.AbstractConnection;
import noelle.database.impl.parameter.ParameterProviderImpl;
import noelle.database.impl.spliterator.ResultSetSpliterator;
import noelle.database.utils.Wrap;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.stream.Stream;

public class QueryImpl extends ParameterProviderImpl<Query, PreparedStatement> implements Query {
    private final Connection connection;
    private final boolean closeConnection;

    public QueryImpl(@NotNull AbstractConnection abstractConnection, Connection connection, PreparedStatement statement, boolean closeConnection) {
        super(statement, abstractConnection.bindings());
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public <R> Stream<R> map(SqlFunction<ResultSet, R> mapping) {
        return ResultSetSpliterator.stream(mapping, Wrap.get(statement::executeQuery)).onClose(this::close);
    }

    @Override
    public void close() {
        super.close();

        if (closeConnection) {
            Wrap.execute(connection::close);
        }
    }
}
