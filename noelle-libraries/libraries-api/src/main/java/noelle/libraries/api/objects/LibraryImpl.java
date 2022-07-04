package noelle.libraries.api.objects;

import java.util.Objects;

final class LibraryImpl implements Library {
    private final String groupId;
    private final String artifactId;
    private final String version;

    private String name;

    public LibraryImpl(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public String groupId() {
        return groupId;
    }

    @Override
    public String artifactId() {
        return artifactId;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public boolean snapshot() {
        return version.endsWith("-SNAPSHOT");
    }

    @Override
    public String artifactName() {
        return Objects.requireNonNullElseGet(name, () -> artifactId + "-" + version);
    }

    @Override
    public void newArtifactName(String name) {
        this.name = name;
    }

}
