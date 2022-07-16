package noelle.database.exceptions;

import java.sql.SQLException;

public class UncheckedSQLException extends RuntimeException {

    public UncheckedSQLException(SQLException exception) {
        super(exception);
    }

    public UncheckedSQLException(String message, SQLException exception) {
        super(message, exception);
    }

    @Override
    public SQLException getCause() {
        return (SQLException) super.getCause();
    }
}
