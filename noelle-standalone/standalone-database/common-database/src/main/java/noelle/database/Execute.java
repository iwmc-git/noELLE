package noelle.database;

import java.sql.PreparedStatement;

public interface Execute<S extends PreparedStatement> extends ParameterProvider<Execute<S>, S> {

    /**
     * Executes this statement.
     *
     * @return {@code true} if the first result is a {@link java.sql.ResultSet}
     *         object; {@code false} if the first result is an update count or
     *         there is no result
     * @see PreparedStatement#execute()
     * @see StatementHolder#getStatement()
     */
    boolean execute();
}