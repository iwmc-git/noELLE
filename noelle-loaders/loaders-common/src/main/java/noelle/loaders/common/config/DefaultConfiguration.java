package noelle.loaders.common.config;

import com.google.gson.annotations.SerializedName;

import noelle.loaders.common.config.object.RemapObject;
import noelle.loaders.common.config.object.ReposigtoryObject;
import noelle.loaders.common.config.object.DependencyObject;

import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;
import pw.iwmc.libman.api.objects.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultConfiguration {

    @SerializedName("dependencies")
    protected List<DependencyObject> dependencies;

    @SerializedName("repositories")
    protected List<ReposigtoryObject> repositories;

    @SerializedName("remap-rules")
    protected List<RemapObject> remapRules;

    public List<Dependency> dependencies() {
        return dependencies.stream()
                .map(dependency -> Dependency.of(dependency.groupId(), dependency.artifactId(), dependency.version()))
                .collect(Collectors.toList());
    }

    public List<Repository> repositories() {
        return repositories.stream()
                .map(repository -> Repository.of(repository.name(), repository.url()))
                .collect(Collectors.toList());
    }

    public List<Remap> remapRules() {
        return remapRules.stream()
                .map(remapRule -> Remap.of(remapRule.fromPattern(), remapRule.toPattern()))
                .collect(Collectors.toList());
    }
}
