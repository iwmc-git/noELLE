package noelle.features.launguages.common.service;

import noelle.configuration.DefaultConfiguration;
import noelle.configuration.hocon.HoconLoader;
import noelle.configuration.yaml.YamlLoader;
import noelle.features.launguages.common.AbstractLanguages;
import noelle.features.launguages.common.Language;
import noelle.features.launguages.common.key.LanguageKey;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class LanguageService {
    private final ScheduledExecutorService executorService;
    private final WatchService watchService;
    private final AbstractLanguages<?> languages;

    public LanguageService(AbstractLanguages<?> languages) {
        this.languages = languages;

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            this.executorService = Executors.newScheduledThreadPool(1);

            executorService.scheduleAtFixedRate(new Runnable() {
                private final ExecutorService executor = Executors.newSingleThreadExecutor();
                private Future<?> lastExecution;
                @Override
                public void run() {
                    if (lastExecution != null && !lastExecution.isDone()) {
                        return;
                    }
                    lastExecution = executor.submit(languageTask());
                }
            }, 1000, 1000, TimeUnit.MILLISECONDS);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Contract(pure = true)
    private @NotNull Runnable languageTask() {
        return () -> {
            try {
                var key = watchService.take();
                for (var event : key.pollEvents()) {
                    DefaultConfiguration<CommentedConfigurationNode> configuration = null;

                    var context = String.valueOf(event.context());
                    var languageKey = LanguageKey.of(context.replace(languages.backendType().format(), ""));
                    var language = Language.fromKey(languageKey);

                    if (language.isEmpty()) {
                        return;
                    }

                    if (event.kind().equals(ENTRY_DELETE)) {
                        if (languages.fallback().getName().equals(context)) {
                            return;
                        }

                        languages.cachedLanguages().remove(language.get());
                        continue;
                    }

                    var targetFile = new File(languages.languagesFolder().toFile(), context);
                    switch (languages.backendType()) {
                        case HOCON -> configuration = HoconLoader.loader(targetFile.toPath()).configuration();
                        case YAML -> configuration = YamlLoader.loader(targetFile.toPath()).configuration();
                    }

                    languages.cachedLanguages().put(language.get(), configuration);
                }

                if (!key.reset()) {
                    executorService.shutdown();
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    public void shutdown() throws IOException {
        executorService.shutdown();
        watchService.close();
    }
}
