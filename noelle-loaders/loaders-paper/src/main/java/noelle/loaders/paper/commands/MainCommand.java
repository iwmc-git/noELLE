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

    public MainCommand(@NotNull String name) {
        super(name);

        var plugin = PaperLoader.loader();

        this.pluginName = plugin.getDescription().getName();
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        var formattedTop = String.format("<color:#12ffa0><b>❖</color> <dark_gray><b>•</dark_gray> <gray>This server <color:#b6ff6e><b>powered</color> by <color:#a175ff><b>%s</b></color>.", pluginName);
        var formattedBottom = String.format("<color:#12ffa0><b>❖</color> <dark_gray><b>•</dark_gray> <gray>Version <dark_gray>- <color:#fd91ff><b>%s</b></color>.", pluginVersion);

        var componentTop = miniMessage.deserialize(formattedTop);
        var componentBottom = miniMessage.deserialize(formattedBottom);

        sender.sendMessage(Component.empty());
        sender.sendMessage(componentTop);
        sender.sendMessage(componentBottom);
        sender.sendMessage(Component.empty());
        return true;
    }
}
