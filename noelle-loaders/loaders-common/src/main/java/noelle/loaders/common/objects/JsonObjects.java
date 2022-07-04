package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;

import noelle.libraries.api.objects.Library;
import noelle.libraries.api.objects.Repository;

import java.util.ArrayList;
import java.util.List;

public class JsonObjects {

    @SerializedName("libraries")
    protected List<JsonLibrary> libraries;

    @SerializedName("repositories")
    protected List<String> repositories;

    public List<Library> libraries() {
        var newLibraries = new ArrayList<Library>();
        libraries.forEach(dependency -> newLibraries.add(Library.of(dependency.groupId(), dependency.artifactId(), dependency.version())));

        return newLibraries;
    }

    public List<Repository> repositories() {
        var newRepositories = new ArrayList<Repository>();
        repositories.forEach(s -> newRepositories.add(Repository.of(s)));

        return newRepositories;
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
