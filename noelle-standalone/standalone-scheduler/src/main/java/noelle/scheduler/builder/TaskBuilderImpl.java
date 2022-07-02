package noelle.scheduler.builder;

import noelle.scheduler.DefaultScheduler;
import noelle.scheduler.caller.Caller;
import noelle.scheduler.task.ScheduledTask;
import noelle.scheduler.task.ScheduledTaskImpl;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TaskBuilderImpl implements TaskBuilder {
    private final static DefaultScheduler SCHEDULER = DefaultScheduler.instance();

    private final Caller caller;
    private final Runnable runnable;
    private final Consumer<ScheduledTask> consumer;
    private long delay;
    private long repeat;

    public TaskBuilderImpl(Caller caller, Runnable runnable, Consumer<ScheduledTask> consumer) {
        this.caller = caller;
        this.runnable = runnable;
        this.consumer = consumer;
    }

    @Override
    public TaskBuilder delay(long time, @NotNull TimeUnit unit) {
        this.delay = unit.toMillis(time);
        return this;
    }

    @Override
    public TaskBuilder repeat(long time, @NotNull TimeUnit unit) {
        this.repeat = unit.toMillis(time);
        return this;
    }

    @Override
    public TaskBuilder clearDelay() {
        this.delay = 0;
        return this;
    }

    @Override
    public TaskBuilder clearRepeat() {
        this.repeat = 0;
        return this;
    }

    @Override
    public ScheduledTask schedule() {
        var task = new ScheduledTaskImpl(caller, runnable, consumer, delay, repeat);
        SCHEDULER.callerTasks().put(caller, task);

        task.schedule();
        return task;
    }
}
