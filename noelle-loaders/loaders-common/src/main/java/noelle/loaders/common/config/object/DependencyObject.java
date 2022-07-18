package noelle.loaders.common.config.object;

public class DependencyObject {
    protected String groupId, artifactId, version;

    public String version() {
        return version;
    }

    public String groupId() {
        return groupId;
    }

    public String artifactId() {
        return artifactId;
    }
}
