package noelle.libraries.downloader;

import noelle.libraries.api.downloader.Downloader;
import noelle.libraries.api.objects.Library;
import noelle.libraries.api.objects.Repository;
import noelle.libraries.exceptions.DownloadException;
import noelle.libraries.exceptions.InvalidHashException;
import noelle.libraries.utilities.ChecksumUtility;
import noelle.libraries.utilities.FileUtilities;
import noelle.libraries.utilities.LibraryUtility;
import noelle.libraries.utilities.SnapshotUtility;

import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;

import org.codehaus.plexus.util.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class LibrariesDownloader implements Downloader {
    private final MetadataXpp3Reader reader = new MetadataXpp3Reader();
    private final boolean checkHashes;

    public LibrariesDownloader(boolean checkHashes) {
        this.checkHashes = checkHashes;
    }

    public void download(@NotNull Library library, List<Repository> repositories, Path output) throws Exception {
        FileUtilities.makeDirectory(LibraryUtility.libraryFile(library, output).getParentFile().toPath());

        if (!LibraryUtility.libraryFile(library, output).exists()) {
            if (repositories != null && !repositories.isEmpty()) {
                downloadLibrary(library, repositories, output);
            } else {
                throw new DownloadException("Artifact cannot be downloaded.");
            }
        }
    }

    private void downloadLibrary(Library library, @NotNull List<Repository> repositories, Path output) {
        var found = false;

        for (var repository : repositories) {
            try {
                if (found) continue;

                if (library.snapshot()) {
                    var metaDataUrl = LibraryUtility.metadataUrl(library, repository.url());
                    var urlConnection = openConnection(metaDataUrl);
                    var status = urlConnection.getResponseCode();

                    assert (status >= 200 && status < 300) || status == 304;

                    var stream = openStream(metaDataUrl);
                    var meta = reader.read(stream);

                    var latest = SnapshotUtility.snapshotVersion(meta);

                    library.newArtifactName(library.artifactId() + "-" + latest);
                    var artifactUrl = LibraryUtility.artifactUrl(library, repository.url());

                    if (!LibraryUtility.libraryFile(library, output).exists()) {
                        downloadFile(artifactUrl, LibraryUtility.libraryFile(library, output));
                    }

                    found = Files.exists(LibraryUtility.libraryPath(library, output));

                    if (checkHashes) {
                        var hashUrl = LibraryUtility.sha1Url(library, repository.url());
                        var hashStream = openStream(hashUrl);
                        var code = IOUtil.toString(hashStream);

                        var validHash = ChecksumUtility.check(LibraryUtility.libraryFile(library, output), code);

                        if (!validHash) {
                            throw new InvalidHashException("Artifact hash mismatch for file: " + LibraryUtility.libraryFile(library, output).getName());
                        }
                    }
                } else {
                    var artifactUrl = LibraryUtility.artifactUrl(library, repository.url());
                    var urlConnection = openConnection(artifactUrl);
                    var status = urlConnection.getResponseCode();

                    assert (status >= 200 && status < 300) || status == 304;

                    if (!LibraryUtility.libraryFile(library, output).exists()) {
                        downloadFile(artifactUrl, LibraryUtility.libraryFile(library, output));
                    }

                    found = Files.exists(LibraryUtility.libraryPath(library, output));

                    if (checkHashes) {
                        var hashUrl = LibraryUtility.sha1Url(library, repository.url());
                        var hashStream = openStream(hashUrl);
                        var code = IOUtil.toString(hashStream);

                        var validHash = ChecksumUtility.check(LibraryUtility.libraryFile(library, output), code);

                        if (!validHash) {
                            throw new InvalidHashException("Artifact hash mismatch for file: " + LibraryUtility.libraryFile(library, output).getName());
                        }
                    }
                }
            } catch (Exception exception) {
                // ignoring............. :p
            }
        }

        if (!found) {
            throw new DownloadException("Could not download artifact `" + library.artifactId() + "` from any of the repositories");
        }
    }

    private void downloadFile(URL url, @NotNull File output) throws Exception {
        var stream = openStream(url);
        Files.copy(stream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private InputStream openStream(URL url) throws Exception {
        var connection = openConnection(url);
        return connection.getInputStream();
    }

    private @NotNull HttpURLConnection openConnection(@NotNull URL url) throws Exception {
        var connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        var status = connection.getResponseCode();
        var redirect = status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER;

        if (redirect) {
            var newUrl = connection.getHeaderField("Location");
            return openConnection(new URL(newUrl));
        }

        return connection;
    }
}
