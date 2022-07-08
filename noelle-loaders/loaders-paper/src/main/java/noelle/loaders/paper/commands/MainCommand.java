package noelle.loaders.paper.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import noelle.loaders.paper.PaperLoader;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

public final class MainCommand extends Command {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String pluginName;
    private final String pluginVersion;

    private final String serverName;
    private final String serverVersion;

    public MainCommand(@NotNull String name) {
        super(name);

        var plugin = PaperLoader.loader();

        this.pluginName = plugin.getDescription().getName();
        this.pluginVersion = plugin.getDescription().getVersion();

        this.serverName = plugin.getServer().getName();
        this.serverVersion = plugin.getServer().getVersion();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        var formattedTop = String.format("<color:#12ffa0><b>❖</color> <dark_gray><b>•</dark_gray> <gray>This server <color:#b6ff6e><b>powered</color> by <color:#a175ff><b>%s</b></color>.", pluginName);
        var formattedBottom = String.format("<color:#12ffa0><b>❖</color> <dark_gray><b>•</dark_gray> <gray>Version <dark_gray>- <color:#fd91ff><b>%s</b></color>.", pluginVersion);
        var foramttedRunningOn = String.format("<color:#12ffa0><b>❖</color> <dark_gray><b>•</dark_gray> <gray>Running on <color:#57a2ff><b>%s</color>  <dark_gray>(<white>%s<dark_gray>)", serverName, serverVersion);

        var componentTop = miniMessage.deserialize(formattedTop);
        var componentBottom = miniMessage.deserialize(formattedBottom);
        var componentRunningOn = miniMessage.deserialize(foramttedRunningOn);

        sender.sendMessage(componentTop);
        sender.sendMessage(componentBottom);
        sender.sendMessage(Component.empty());
        sender.sendMessage(componentRunningOn);
        return true;
    }
}
