package noelle.scheduler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import noelle.scheduler.builder.TaskBuilder;
import noelle.scheduler.builder.TaskBuilderImpl;
import noelle.scheduler.caller.Caller;
import noelle.scheduler.exceptions.AlreadyInitializedException;
import noelle.scheduler.task.ScheduledTask;

import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultScheduler implements Scheduler {
    private static DefaultScheduler INSTANCE;

    private final Multimap<Caller, ScheduledTask> tasksByCaller;
    private final Collection<ScheduledTask> allTasks;

    private final ExecutorService taskService;
    private final ScheduledExecutorService timerExecutionService;

    private final Logger logger;

    public DefaultScheduler() throws AlreadyInitializedException {
        if (INSTANCE != null) {
            throw new AlreadyInitializedException("Scheduler already initialized");
        }

        INSTANCE = this;

        this.tasksByCaller = Multimaps.synchronizedMultimap(Multimaps.newSetMultimap(new IdentityHashMap<>(), HashSet::new));
        this.allTasks = Collections.emptyList();

        this.taskService = Executors.newCachedThreadPool(
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("noELLE Task Scheduler - #%d").build());
        this.timerExecutionService = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("noELLE Task Scheduler Timer").build());

        this.logger = LoggerFactory.getLogger("noelle::logger");
    }

    public static DefaultScheduler instance() {
        return INSTANCE;
    }

    @Override
    public boolean shutdown() throws InterruptedException {
        Collection<ScheduledTask> terminating;

        synchronized (tasksByCaller) {
            terminating = ImmutableList.copyOf(tasksByCaller.values());
        }

        for (var task : terminating) {
            task.cancel();
        }

        timerExecutionService.shutdown();
        taskService.shutdown();

        return taskService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Override
    public TaskBuilder buildTask(@NotNull Caller caller, @NotNull Runnable runnable) {
        return new TaskBuilderImpl(caller, runnable, null);
    }

    @Override
    public TaskBuilder buildTask(@NotNull Caller caller, @NotNull Consumer<ScheduledTask> consumer) {
        return new TaskBuilderImpl(caller, null, consumer);
    }

    @Override
    public @NotNull Collection<ScheduledTask> tasksByCaller(@NotNull Caller caller) {
        var tasks = tasksByCaller.get(caller);

        synchronized (tasksByCaller) {
            return Set.copyOf(tasks);
        }
    }

    @Override
    public @NotNull Multimap<Caller, ScheduledTask> callerTasks() {
        return tasksByCaller;
    }

    @Override
    public @NotNull Collection<ScheduledTask> tasks() {
        synchronized (allTasks) {
            return Set.copyOf(allTasks);
        }
    }

    public ExecutorService taskService() {
        return taskService;
    }

    public ScheduledExecutorService timerExecutionService() {
        return timerExecutionService;
    }

    public Logger logger() {
        return logger;
    }
}
