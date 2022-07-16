package noelle.database.h2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import noelle.database.PoolOptions;
import noelle.database.credentials.Credentials;
import noelle.database.impl.AbstractConnection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Private implementation for H2 of ${@link AbstractConnection}.
 */
public final class H2Connection extends AbstractConnection {

    public H2Connection(DataSource dataSource) {
        super(dataSource);
    }

    @Contract("_, _ -> new")
    public static @NotNull H2Connection makeConnection(@NotNull Credentials credentials, @NotNull Path rootPath) {
        try {
            var config = new HikariConfig();
            var file = rootPath.resolve(credentials.database());

            if (!Files.exists(file)) {
                Files.createFile(file);
            }

            var jdbcUrl = "jdbc:h2:file:" + rootPath.resolve(credentials.database());

            config.setPoolName("noelle-h2-pool-" + PoolOptions.POOL_COUNTER.getAndIncrement());
            config.setJdbcUrl(jdbcUrl);

            var dataSource = new HikariDataSource(config);

            return new H2Connection(dataSource);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
