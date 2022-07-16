package noelle.database.utils;

import noelle.database.impl.AbstractConnection;

import java.sql.Connection;
import java.sql.Statement;

@FunctionalInterface
public interface Creator<T, S extends Statement> {
    T create(AbstractConnection impl, Connection connection, S statement, boolean closeConnectionAfterAction);
}