package noelle.libraries.utilities;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.jetbrains.annotations.NotNull;

public final class SnapshotUtility {

    public static String snapshotVersion(@NotNull Metadata metadata) {
        var versions = metadata.getVersioning().getSnapshotVersions();
        var version = versions.stream().filter(s -> s.getClassifier().isEmpty() && s.getExtension().equals("jar")).findFirst();

        return version.isEmpty() ? "" : version.get().getVersion();
    }
}
