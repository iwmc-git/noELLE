package noelle.libraries.api.downloader;

import noelle.libraries.api.objects.Library;
import noelle.libraries.api.objects.Repository;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public interface Downloader {
    void download(@NotNull Library library, List<Repository> repositories, Path output) throws Exception;
}
