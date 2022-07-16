package noelle.database.mariadb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import noelle.database.PoolOptions;
import noelle.database.credentials.Credentials;
import noelle.database.impl.AbstractConnection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * Private implementation for MariaDB of ${@link AbstractConnection}.
 */
final class MariaDBConnection extends AbstractConnection {

    public MariaDBConnection(DataSource dataSource) {
        super(dataSource);
    }

    @Contract("_ -> new")
    public static @NotNull MariaDBConnection makeConnection(@NotNull Credentials credentials) {
        try {
            var config = new HikariConfig();

            var splitableHost = credentials.host().split(":");
            var host = splitableHost[0];
            var port = Integer.parseInt(splitableHost[1]);

            config.setPoolName("noelle-mariadb-pool-" + PoolOptions.POOL_COUNTER.getAndIncrement());
            config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            config.addDataSourceProperty("serverName", host);
            config.addDataSourceProperty("port", port);
            config.addDataSourceProperty("databaseName", credentials.database());
            config.setUsername(credentials.username());
            config.setPassword(credentials.password());
            config.setMaximumPoolSize(PoolOptions.MAXIMUM_POOL_SIZE);
            config.setMinimumIdle(PoolOptions.MINIMUM_IDLE);
            config.setMaxLifetime(PoolOptions.MAX_LIFETIME);
            config.setConnectionTimeout(PoolOptions.CONNECTION_TIMEOUT);
            config.setLeakDetectionThreshold(PoolOptions.LEAK_DETECTION_THRESHOLD);

            var dataSource = new HikariDataSource(config);

            return new MariaDBConnection(dataSource);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
