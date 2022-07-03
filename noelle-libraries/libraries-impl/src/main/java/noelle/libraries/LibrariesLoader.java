package noelle.libraries;

import noelle.libraries.api.LibrariesAPI;
import noelle.libraries.api.LibrariesProvider;
import noelle.libraries.api.downloader.Downloader;
import noelle.libraries.api.injector.Injector;
import noelle.libraries.api.objects.Library;
import noelle.libraries.api.objects.Repository;

import noelle.libraries.downloader.LibrariesDownloader;
import noelle.libraries.utilities.LibraryUtility;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LibrariesLoader implements LibrariesAPI {
    public static final Map<Library, Path> DOWNLOADED = new HashMap<>();
    public static final Map<Library, Path> REMAPPED = new HashMap<>();

    private final List<Repository> repositories;

    private final Path downloadedPath;

    private final LibrariesDownloader downloader;

    public LibrariesLoader(@NotNull Path root, List<Repository> repositories, boolean checkHash) {
        this.repositories = repositories;

        this.downloader = new LibrariesDownloader(checkHash);
        this.downloadedPath = Paths.get(root.toString(), "downloaded").toAbsolutePath();

        LibrariesProvider.register(this);
    }

    public void stop() {
        LibrariesProvider.unregister();
    }

    @Override
    public Map<Library, Path> downloaded() {
        return DOWNLOADED;
    }

    @Override
    public Map<Library, Path> remapped() {
        return REMAPPED;
    }

    @Override
    public Downloader downloader() {
        return downloader;
    }

    @Override
    public void download(Library library) {
        try {
            downloader.download(library, repositories, downloadedPath);

            var libraryPath = LibraryUtility.libraryPath(library, downloadedPath);
            DOWNLOADED.put(library, libraryPath);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void download(@NotNull List<Library> libraries) {
        for (var library : libraries) {
            download(library);
        }
    }

    @Override
    public void inject(Library library, @NotNull Injector injector) {
        var path = LibraryUtility.libraryPath(library);
        injector.addToClasspath(path);
    }

    @Override
    public void inject(@NotNull List<Library> libraries, Injector injector) {
        for (var library : libraries) {
            var path = LibraryUtility.libraryPath(library);
            injector.addToClasspath(path);
        }
    }
}
