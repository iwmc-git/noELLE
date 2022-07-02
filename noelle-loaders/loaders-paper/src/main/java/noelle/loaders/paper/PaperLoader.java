package noelle.loaders.paper;

import noelle.loaders.paper.commands.MainCommand;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

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
