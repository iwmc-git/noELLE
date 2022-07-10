package noelle.loaders.paper;

import noelle.loaders.common.CommonLoader;
import noelle.loaders.paper.commands.MainCommand;
import noelle.loaders.paper.utils.UnsafeClassLoader;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.net.URLClassLoader;

public final class PaperLoader extends JavaPlugin {
    private static PaperLoader loader;

    private final Server server = getServer();
    private final Logger logger = getSLF4JLogger();

    private final PluginDescriptionFile desc = getDescription();

    public PaperLoader() {
        loader = this;

        var formattedMessage = String.format("%s v%s is loading now...", desc.getName(), desc.getVersion());
        logger.info(formattedMessage);

        logger.info("Loading libraries...");
        var commonLoader = new CommonLoader(getDataFolder().toPath());
        commonLoader.start();

        logger.info("Injecting libraries...");
        var unsafeLoader = new UnsafeClassLoader((URLClassLoader) getClassLoader());
        var downloaded = commonLoader.downloaded();
        downloaded.forEach(unsafeLoader::addPath);
    }

    @Override
    public void onEnable() {
        logger.info("Registering commands...");

        var commandMap = server.getCommandMap();
        var mainCommand = new MainCommand("noelle");

        commandMap.register("", mainCommand);
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
