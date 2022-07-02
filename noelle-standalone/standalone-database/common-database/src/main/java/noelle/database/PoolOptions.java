package noelle.database;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool options.
 */
public interface PoolOptions {
    AtomicInteger POOL_COUNTER = new AtomicInteger(0);

    int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;
    int MINIMUM_IDLE = Math.min(MAXIMUM_POOL_SIZE, 10);

    long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
    long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(10L);
    long LEAK_DETECTION_THRESHOLD = TimeUnit.SECONDS.toMillis(10L);
}

