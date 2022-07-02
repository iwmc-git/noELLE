package noelle.loaders.velocity.commands;

import com.velocitypowered.api.command.SimpleCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import noelle.loaders.velocity.VelocityLoader;

public class MainCommand implements SimpleCommand {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String pluginName;
    private final String pluginVersion;

    private final String serverName;
    private final String serverVersion;

    public MainCommand() {
        var plugin = VelocityLoader.loader();

        this.pluginName = plugin.description().getName().get();
        this.pluginVersion = plugin.description().getVersion().get();

        this.serverName = plugin.proxyServer().getVersion().getName();
        this.serverVersion = plugin.proxyServer().getVersion().getVersion();
    }

    @Override
    public void execute(Invocation invocation) {
        var sender = invocation.source();

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
    }
}
