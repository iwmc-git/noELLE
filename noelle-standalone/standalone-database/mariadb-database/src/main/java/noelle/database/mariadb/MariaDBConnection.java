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
public final class MariaDBConnection extends AbstractConnection {

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

            config.setMaximumPoolSize(500);
            config.setMinimumIdle(20);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(600000);
            config.setIdleTimeout(60000);
            config.setValidationTimeout(3000);

            var dataSource = new HikariDataSource(config);

            return new MariaDBConnection(dataSource);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
