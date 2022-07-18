package noelle.loaders.common.config;

import com.google.gson.annotations.SerializedName;

import noelle.loaders.common.config.object.DependencyObject;
import pw.iwmc.libman.api.objects.Dependency;

import java.util.List;
import java.util.stream.Collectors;

public class CustomConfiguration {

    @SerializedName("dependencies")
    protected List<DependencyObject> dependencies;

    public List<Dependency> dependencies() {
        return dependencies.stream()
                .map(dependency -> Dependency.of(dependency.groupId(), dependency.artifactId(), dependency.version()))
                .collect(Collectors.toList());
    }
}
