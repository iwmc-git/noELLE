package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;
import pw.iwmc.libman.api.objects.Dependency;

import java.util.ArrayList;
import java.util.List;

public class JsonPlatformObjects {

    @SerializedName("dependencies")
    protected List<JsonLibrary> dependencies;

    public List<Dependency> dependencies() {
        var newLibraries = new ArrayList<Dependency>();
        dependencies.forEach(dependency -> newLibraries.add(Dependency.of(dependency.groupId(), dependency.artifactId(), dependency.version())));

        return newLibraries;
    }

    private static final class JsonLibrary {
        String groupId, artifactId, version;

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
}
