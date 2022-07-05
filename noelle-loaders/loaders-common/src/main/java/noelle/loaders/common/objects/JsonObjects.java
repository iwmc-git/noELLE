package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;

import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Repository;

import java.util.ArrayList;
import java.util.List;

public class JsonObjects {

    @SerializedName("dependencies")
    protected List<JsonLibrary> dependencies;

    @SerializedName("repositories")
    protected List<JsonRepository> repositories;

    public List<Dependency> dependencies() {
        var newLibraries = new ArrayList<Dependency>();
        dependencies.forEach(dependency -> newLibraries.add(Dependency.of(dependency.groupId(), dependency.artifactId(), dependency.version())));

        return newLibraries;
    }

    public List<Repository> repositories() {
        var newRepositories = new ArrayList<Repository>();
        repositories.forEach(repository -> newRepositories.add(Repository.of(repository.name(), repository.url())));

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

    private static final class JsonRepository {
        String url, name;

        public String url() {
            return url;
        }

        public String name() {
            return name;
        }
    }
}
