package noelle.loaders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import noelle.loaders.common.CommonLoader;

import java.nio.file.Path;

public final class VelocityLoader {

    @Inject
    public VelocityLoader(@DataDirectory Path pluginRoot) {
        var commonLoader = new CommonLoader(pluginRoot);

        commonLoader.start();
        commonLoader.downloadFromOther("libraries-velocity.json");
    }
}
