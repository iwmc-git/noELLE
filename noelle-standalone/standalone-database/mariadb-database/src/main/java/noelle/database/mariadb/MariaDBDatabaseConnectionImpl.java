package noelle.database.mariadb;

import be.bendem.sqlstreams.SqlStream;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import noelle.database.AbstractConnection;
import noelle.database.PoolOptions;
import noelle.database.credentials.Credentials;

import org.jetbrains.annotations.NotNull;

/**
 * Private implementation for MariaDB of ${@link AbstractConnection}.
 */
final class MariaDBDatabaseConnectionImpl extends AbstractConnection {

    public MariaDBDatabaseConnectionImpl(Credentials credentials) {
        try {
            this.init(credentials);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void init(@NotNull Credentials credentials) {
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

        super.dataSource = new HikariDataSource(config);
        super.sqlStream = SqlStream.connect(dataSource);
    }
}
