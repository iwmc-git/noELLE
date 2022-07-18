package noelle.loaders.paper;

import noelle.loaders.common.CommonLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperLoader extends JavaPlugin {

    public PaperLoader() {
        var commonLoader = new CommonLoader(getDataFolder().toPath());
        commonLoader.downloadDefaults();
        commonLoader.downloadFromCustom("libraries-paper.json");
    }
}
