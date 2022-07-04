package noelle.libraries.api.objects;

import org.jetbrains.annotations.NotNull;

public interface Library {
    String groupId();
    String artifactId();
    String version();

    boolean snapshot();

    static @NotNull Library of(String groupId, String artifactId, String version) {
        return new LibraryImpl(groupId, artifactId, version);
    }

    String artifactName();
    void newArtifactName(String name);
}
