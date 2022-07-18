package noelle.loaders.paper;

import noelle.loaders.common.CommonLoader;
import noelle.loaders.paper.utils.UnsafeClassLoader;

import org.bukkit.plugin.java.JavaPlugin;

public final class PaperLoader extends JavaPlugin {

    public PaperLoader() {
        var commonLoader = new CommonLoader(getDataFolder().toPath());
        commonLoader.start();
        commonLoader.downloadFromOther("libraries-paper.json");

        var unsafeLoader = UnsafeClassLoader.create(getClassLoader());
        var downloaded = commonLoader.downloaded();
        downloaded.forEach(unsafeLoader::addPath);
    }
}
