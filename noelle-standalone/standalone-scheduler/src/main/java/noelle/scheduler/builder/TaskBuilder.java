package noelle.scheduler.builder;

import noelle.scheduler.task.ScheduledTask;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Represents a fluent interface to schedule tasks.
 */
public interface TaskBuilder {

    /**
     * Specifies that the task should delay its execution by the specified amount of time.
     *
     * @param time the time to delay by.
     * @param unit the unit of time for {@code time}.
     */
    TaskBuilder delay(@IntRange(from = 0) long time, @NotNull TimeUnit unit);

    /**
     * Specifies that the task should delay its execution by the specified amount of time.
     *
     * @param duration the duration of the delay.
     */
    default TaskBuilder delay(@NotNull Duration duration) {
        return delay(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Specifies that the task should continue running after waiting for the specified amount, until
     * it is cancelled.
     *
     * @param time the time to delay by.
     * @param unit the unit of time for {@code time}.
     */
    TaskBuilder repeat(@IntRange(from = 0) long time, @NotNull TimeUnit unit);

    /**
     * Specifies that the task should continue running after waiting for the specified amount, until
     * it is cancelled.
     *
     * @param duration the duration of the delay.
     */
    default TaskBuilder repeat(@NotNull Duration duration) {
        return repeat(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Clears the delay on this task.
     */
    TaskBuilder clearDelay();

    /**
     * Clears the repeat interval on this task.
     */
    TaskBuilder clearRepeat();

    /**
     * Schedules this task for execution.
     */
    ScheduledTask schedule();
}