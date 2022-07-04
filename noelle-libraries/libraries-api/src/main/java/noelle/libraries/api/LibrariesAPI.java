package noelle.libraries.api;

import noelle.libraries.api.injector.Injector;
import noelle.libraries.api.objects.Library;
import noelle.libraries.api.downloader.Downloader;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface LibrariesAPI {
    Map<Library, Path> downloaded();
    Map<Library, Path> remapped();

    Downloader downloader();

    void download(Library library);
    void download(List<Library> libraries);

    void inject(Library library, Injector injector);
    void inject(List<Library> library, Injector injector);

    static LibrariesAPI libraries() {
        return LibrariesProvider.libraries();
    }
}
