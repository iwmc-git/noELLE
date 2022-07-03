package noelle.loaders.velocity;

import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;

import noelle.loaders.velocity.commands.MainCommand;
import noelle.utils.update.UpdateUtil;
import org.slf4j.Logger;

public class VelocityLoader {
    private static VelocityLoader loader;

    private final ProxyServer proxyServer;
    private final Logger logger;

    private PluginDescription description;

    @Inject
    public VelocityLoader(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;

        loader = this;
    }

    @Subscribe
    public void onServerStartup(ProxyInitializeEvent event) {
        this.description = proxyServer.getPluginManager().fromInstance(this).get().getDescription();

        var formattedMessage = String.format("%s v%s is loading now...", description.getName().get(), description.getVersion().get());
        logger.info(formattedMessage);

        var updates = UpdateUtil.checkVersionByURL("https://raw.githubusercontent.com/iwmc-git/noELLE/master/VERSION", description.getVersion().get());
        logger.info("Checking the availability of updates....");

        if (updates) {
            logger.info("noELLE is up to date, enjoy!");
        } else {
            logger.info("noELLE is out to date!");
            logger.info("Plese, download latest version from - https://github.com/iwmc-git/noELLE/releases");
        }

        var commandManager = proxyServer.getCommandManager();
        commandManager.register("noellev", new MainCommand());
    }

    @Subscribe
    public void onServerStopping(ProxyShutdownEvent event) {
        var formattedMessage = String.format("%s v%s is stopping now...", description.getName().get(), description.getVersion().get());
        logger.info(formattedMessage);
    }

    public ProxyServer proxyServer() {
        return proxyServer;
    }

    public PluginDescription description() {
        return description;
    }

    public static VelocityLoader loader() {
        return loader;
    }
}
