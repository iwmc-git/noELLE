package noelle.database.impl.batch;

import noelle.database.BatchUpdate;
import noelle.database.impl.AbstractConnection;
import noelle.database.impl.parameter.ParameterProviderImpl;
import noelle.database.utils.Wrap;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class BatchUpdateImpl extends ParameterProviderImpl<BatchUpdate, PreparedStatement> implements BatchUpdate {

    private final Connection connection;
    private final boolean closeConnection;

    public BatchUpdateImpl(@NotNull AbstractConnection abstractConnection, Connection connection, PreparedStatement statement, boolean closeConnection) {
        super(statement, abstractConnection.bindings());
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public BatchUpdate next() {
        Wrap.execute(statement::addBatch);
        return this;
    }

    @Override
    public BatchUpdate next(String sql) {
        Wrap.execute(() -> statement.addBatch(sql));
        return this;
    }

    @Override
    public int[] counts() {
        return Wrap.get(statement::executeBatch);
    }

    @Override
    public long[] largeCounts() {
        return Wrap.get(statement::executeLargeBatch);
    }

    @Override
    public int count() {
        return IntStream.of(Wrap.get(statement::executeBatch)).sum();
    }

    @Override
    public long largeCount() {
        return LongStream.of(Wrap.get(statement::executeLargeBatch)).sum();
    }

    public void close() {
        super.close();

        if (closeConnection) {
            Wrap.execute(connection::close);
        }
    }
}

