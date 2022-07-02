package noelle.scheduler.task;

import noelle.scheduler.DefaultScheduler;
import noelle.scheduler.caller.Caller;
import noelle.scheduler.status.TaskStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ScheduledTaskImpl implements Runnable, ScheduledTask {
    private final static DefaultScheduler SCHEDULER = DefaultScheduler.instance();

    private final Caller caller;
    private final Runnable runnable;
    private final Consumer<ScheduledTask> consumer;
    private final long delay;
    private final long repeat;
    private @Nullable ScheduledFuture<?> future;
    private volatile @Nullable Thread currentTaskThread;

    public ScheduledTaskImpl(Caller caller, Runnable runnable, Consumer<ScheduledTask> consumer, long delay, long repeat) {
        this.caller = caller;
        this.runnable = runnable;
        this.consumer = consumer;
        this.delay = delay;
        this.repeat = repeat;
    }

    public void schedule() {
        if (repeat == 0) {
            this.future = SCHEDULER.timerExecutionService().schedule(this, delay, TimeUnit.MILLISECONDS);
        } else {
            this.future = SCHEDULER.timerExecutionService()
                    .scheduleAtFixedRate(this, delay, repeat, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public @NotNull Caller caller() {
        return caller;
    }

    @Override
    public TaskStatus status() {
        if (future == null) {
            return TaskStatus.SCHEDULED;
        }

        if (future.isCancelled()) {
            return TaskStatus.CANCELLED;
        }

        if (future.isDone()) {
            return TaskStatus.FINISHED;
        }

        return TaskStatus.SCHEDULED;
    }

    @Override
    public void cancel() {
        if (future != null) {
            future.cancel(false);

            var currentThread = currentTaskThread;
            if (currentThread != null) {
                currentThread.interrupt();
            }

            onFinish();
        }
    }

    @Override
    public void run() {
        SCHEDULER.taskService().execute(() -> {
            currentTaskThread = Thread.currentThread();

            try {
                if (runnable != null) {
                    runnable.run();
                } else {
                    consumer.accept(this);
                }
            } catch (Exception exception) {
                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                } else {
                    SCHEDULER.logger().error("Exception in task " + runnable + " by caller " + caller.callerName(), exception);
                }
            } finally {
                if (repeat == 0) {
                    onFinish();
                }

                currentTaskThread = null;
            }
        });
    }

    private void onFinish() {
        SCHEDULER.callerTasks().remove(caller, this);
    }
}