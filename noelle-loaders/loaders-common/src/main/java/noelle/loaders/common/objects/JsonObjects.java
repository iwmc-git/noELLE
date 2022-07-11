package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;

import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;
import pw.iwmc.libman.api.objects.Repository;

import java.util.ArrayList;
import java.util.List;

public class JsonObjects {

    @SerializedName("dependencies")
    protected List<JsonLibrary> dependencies;

    @SerializedName("repositories")
    protected List<JsonRepository> repositories;

    @SerializedName("remapRules")
    protected List<JsonRemap> remapRules;

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

    public List<Remap> remaProperties() {
        var newRepositories = new ArrayList<Remap>();
        remapRules.forEach(remap -> newRepositories.add(Remap.of(remap.fromPattern(), remap.toPattern())));

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

    private static final class JsonRemap {
        String fromPattern, toPattern;

        public String fromPattern() {
            return fromPattern;
        }

        public String toPattern() {
            return toPattern;
        }
    }
}
