package noelle.database.h2;

import be.bendem.sqlstreams.SqlStream;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import noelle.database.AbstractConnection;
import noelle.database.PoolOptions;
import noelle.database.credentials.Credentials;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Private implementation for H2 of ${@link AbstractConnection}.
 */
final class H2DatabaseConnectionImpl extends AbstractConnection {
    private final Path rootPath;

    public H2DatabaseConnectionImpl(@NotNull Credentials credentials, @NotNull Path rootPath) {
        this.rootPath = rootPath;

        try {
            this.init(credentials);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void init(@NotNull Credentials credentials) throws Exception {
        var config = new HikariConfig();
        var file = rootPath.resolve(credentials.database());

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        var jdbcUrl = "jdbc:h2:file:" + rootPath.resolve(credentials.database());

        config.setPoolName("noelle-h2-pool-" + PoolOptions.POOL_COUNTER.getAndIncrement());
        config.setJdbcUrl(jdbcUrl);

        super.dataSource = new HikariDataSource(config);
        super.sqlStream = SqlStream.connect(dataSource);
    }
}
