package noelle.scheduler;

import java.util.Collection;
import java.util.function.Consumer;

import com.google.common.collect.Multimap;
import noelle.scheduler.builder.TaskBuilder;
import noelle.scheduler.caller.Caller;
import noelle.scheduler.exceptions.AlreadyInitializedException;
import noelle.scheduler.exceptions.NotInitializedException;
import noelle.scheduler.task.ScheduledTask;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a scheduler to execute tasks.
 */
public interface Scheduler {

    /**
     * Initializes new scheduler.
     * May be throw exception if scheduler already initialized.
     *
     * @return the scheduler.
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull Scheduler init() throws AlreadyInitializedException {
        return new DefaultScheduler();
    }

    /**
     * Returns scehduler instance.
     * Scheduler
     *
     * @return the scheduler instance.
     */
    static @NotNull Scheduler scheduler() {
        var instance = DefaultScheduler.instance();

        if (instance == null) {
            throw new NotInitializedException("Scheduler not initialized!");
        }

        return instance;
    }

    /**
     * Shuts down the scheduler.
     *
     * @return {@code true} if all tasks finished, {@code false} otherwise.
     * @throws InterruptedException if the current thread was interrupted.
     */
    boolean shutdown() throws InterruptedException;

    /**
     * Initializes a new {@link TaskBuilder} for creating a task.
     *
     * @param caller the caller to request the task for.
     * @param runnable the task to run when scheduled.
     * @return the task builder.
     */
    TaskBuilder buildTask(@NotNull Caller caller, @NotNull Runnable runnable);

    /**
     * Initializes a new {@link TaskBuilder} for creating a task.
     *
     * @param caller the caller to request the task for.
     * @param consumer the task to be run when scheduled with the capacity to cancel itself.
     * @return the task builder.
     */
    TaskBuilder buildTask(@NotNull Caller caller, @NotNull Consumer<ScheduledTask> consumer);

    /**
     * Get the {@link ScheduledTask} for a specific caller.
     *
     * @param caller the caller object.
     * @return the list of {@link ScheduledTask} corresponding to a specific caller.
     */
    @NotNull Collection<ScheduledTask> tasksByCaller(@NotNull Caller caller);

    /**
     * Returns all tasks with callers.
     * @return a tasks map.
     */
    @NotNull Multimap<Caller, ScheduledTask> callerTasks();

    /**
     * Get the all {@link ScheduledTask}.
     *
     * @return the list of {@link ScheduledTask}.
     */
    @NotNull Collection<ScheduledTask> tasks();
}
