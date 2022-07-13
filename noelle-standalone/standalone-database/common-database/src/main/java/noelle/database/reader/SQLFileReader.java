package noelle.database.reader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface SQLFileReader {
    Optional<SQLQuery> query(String name);
    List<SQLQuery> allQueries();

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SQLFileReader readStream(InputStream stream) {
        return new SQLFileReaderImpl(stream);
    }
}
