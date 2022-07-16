package noelle.database.functions;

import java.sql.SQLException;

/**
 * Represents an action.
 */
@FunctionalInterface
public interface SqlAction {

    /**
     * Performs this action.
     *
     * @throws SQLException generally rethrown as {@link noelle.database.exceptions.UncheckedSQLException}
     */
    void execute() throws SQLException;
}
