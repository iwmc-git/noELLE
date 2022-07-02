package noelle.scheduler.task;

import noelle.scheduler.caller.Caller;
import noelle.scheduler.status.TaskStatus;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a task that is scheduled.
 */
public interface ScheduledTask {

    /**
     * Returns the caller that scheduled this task.
     */
    @NotNull Caller caller();

    /**
     * Returns the current status of this task.
     */
    TaskStatus status();

    /**
     * Cancels this task. If the task is already running, the thread in which it is running will be
     * interrupted. If the task is not currently running, Velocity will terminate it safely.
     */
    void cancel();
}