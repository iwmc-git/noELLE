package noelle.database.utils.retriever;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetRetrieverByIndex<T> {
    T retrieve(ResultSet resultSet, int index) throws SQLException;
}
