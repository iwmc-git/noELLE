package noelle.database.reader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SQLFileReaderImpl implements SQLFileReader {
    private final List<SQLQuery> queries;

    public SQLFileReaderImpl(InputStream stream) {
        this.queries = new ArrayList<>();

        var reader = new BufferedReader(new InputStreamReader(stream));

        try {
            List<String> queryLines = null;

            var line = "";
            var queryName = "";

            while ((line = nextLine(reader)) != null) {
                if (isQueryName(line)) {
                    registerQuery(queryName, queryLines);

                    queryName = extractQueryName(line);
                    queryLines = new ArrayList<>();
                } else {
                    if (queryLines != null) {
                        queryLines.add(line);
                    }
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Optional<SQLQuery> query(String name) {
        return queries.stream().filter(sqlQuery -> sqlQuery.name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<SQLQuery> allQueries() {
        return queries;
    }

    private void registerQuery(String queryName, List<String> queryLines) {
        if (queryName != null && !queryName.trim().isEmpty() && queryLines != null && !queryLines.isEmpty()) {
            var stringBuilder = new StringBuilder();

            for (var queryLine : queryLines) {
                stringBuilder.append(queryLine).append('\n');
            }

            var query = new SQLQueryImpl(queryName, stringBuilder.substring(0, stringBuilder.length() - 1));
            queries.add(query);
        }
    }

    private @Nullable String extractQueryName(String line) {
        var matcher = Pattern.compile("#(\\w+)").matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }


    @Contract(pure = true)
    private boolean isQueryName(@NotNull String line) {
        return line.matches("^--\\s*#\\w+");
    }

    private String nextLine(BufferedReader reader) throws IOException {
        var line = "";

        while (line != null && !isValidLine(line)) {
            line = reader.readLine();
        }

        return line;
    }

    private boolean isValidLine(String line) {
        return isQueryLine(line) || isQueryName(line);
    }

    private boolean isQueryLine(@NotNull String line) {
        return !line.trim().isEmpty() && !line.matches("^--.*");
    }

}
