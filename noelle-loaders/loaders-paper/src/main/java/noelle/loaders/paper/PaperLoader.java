package noelle.loaders.paper;

import noelle.loaders.common.CommonLoader;
import noelle.loaders.paper.commands.MainCommand;
import noelle.loaders.paper.utils.UnsafeClassLoader;
import noelle.utils.update.UpdateUtil;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.net.URLClassLoader;

public class PaperLoader extends JavaPlugin {
    private static PaperLoader loader;

    private final Server server = getServer();
    private final Logger logger = getSLF4JLogger();

    private final PluginDescriptionFile desc = getDescription();

    @Override
    public void onLoad() {
        loader = this;
    }

    @Override
    public void onEnable() {
        var formattedMessage = String.format("%s v%s is loading now...", desc.getName(), desc.getVersion());
        logger.info(formattedMessage);

        logger.info("Checking the availability of updates....");
        var updates = UpdateUtil.checkVersionByURL("https://raw.githubusercontent.com/iwmc-git/noELLE/master/VERSION", desc.getVersion());

        if (!updates) {
            logger.info("noELLE is up to date, enjoy!");
        } else {
            logger.info("noELLE is out to date!");
            logger.info("Plese, download latest version from - https://github.com/iwmc-git/noELLE/releases");
        }

        var commandMap = server.getCommandMap();
        var mainCommand = new MainCommand("noelle");

        commandMap.register("", mainCommand);

        logger.info("Loading libraries...");
        var commonLoader = new CommonLoader(getDataFolder().toPath(), true, false);
        commonLoader.start();

        var unsafeLoader = new UnsafeClassLoader((URLClassLoader) getClassLoader());
        var downloaded = commonLoader.downloaded();
        downloaded.forEach(unsafeLoader::addPath);
    }

    @Override
    public void onDisable() {
        var formattedMessage = String.format("%s v%s is stopping now...", desc.getName(), desc.getVersion());
        logger.info(formattedMessage);
    }

    public static PaperLoader loader() {
        return loader;
    }
}
