package noelle.database.impl.update;

import noelle.database.Update;
import noelle.database.impl.AbstractConnection;
import noelle.database.impl.parameter.ParameterProviderImpl;
import noelle.database.utils.Wrap;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateImpl extends ParameterProviderImpl<Update, PreparedStatement> implements Update {
    private final Connection connection;
    private final boolean closeConnection;

    public UpdateImpl(@NotNull AbstractConnection abstractConnection, Connection connection, PreparedStatement statement, boolean closeConnection) {
        super(statement, abstractConnection.bindings());
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public int count() {
        return Wrap.get(statement::executeUpdate);
    }

    @Override
    public long largeCount() {
        return Wrap.get(statement::executeLargeUpdate);
    }

    public void close() {
        super.close();

        if (closeConnection) {
            Wrap.execute(connection::close);
        }
    }
}
