package noelle.database.impl.execute;

import noelle.database.Execute;
import noelle.database.impl.AbstractConnection;
import noelle.database.impl.parameter.ParameterProviderImpl;
import noelle.database.utils.Wrap;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExecuteImpl<S extends PreparedStatement> extends ParameterProviderImpl<Execute<S>, S> implements Execute<S> {
    private final Connection connection;
    private final boolean closeConnection;

    public ExecuteImpl(@NotNull AbstractConnection abstractConnection, Connection connection, S statement, boolean closeConnection) {
        super(statement, abstractConnection.bindings());
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public boolean execute() {
        return super.execute();
    }

    @Override
    public void close() {
        super.close();

        if (closeConnection) {
            Wrap.execute(connection::close);
        }
    }
}
