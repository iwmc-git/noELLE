package noelle.loaders.velocity;

import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import noelle.features.events.velocity.CoreLoadedEvent;
import noelle.features.events.velocity.CoreUnloadedEvent;
import noelle.loaders.common.CommonLoader;
import noelle.loaders.velocity.commands.MainCommand;
import noelle.utils.update.UpdateUtil;

import org.slf4j.Logger;

import java.nio.file.Path;

public class VelocityLoader {
    private static VelocityLoader loader;

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path pluginRoot;

    private PluginDescription description;

    @Inject
    public VelocityLoader(ProxyServer proxyServer, Logger logger, @DataDirectory Path pluginRoot) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.pluginRoot = pluginRoot;

        loader = this;
    }

    @Subscribe
    public void onServerStartup(ProxyInitializeEvent event) {
        this.description = proxyServer.getPluginManager().fromInstance(this).get().getDescription();

        var formattedMessage = String.format("%s v%s is loading now...", description.getName().get(), description.getVersion().get());
        logger.info(formattedMessage);

        logger.info("Checking the availability of updates....");
        var updates = UpdateUtil.checkVersionByURL("https://raw.githubusercontent.com/iwmc-git/noELLE/master/VERSION", description.getVersion().get());

        if (updates) {
            logger.info("noELLE is up to date, enjoy!");
        } else {
            logger.info("noELLE is out to date!");
            logger.info("Plese, download latest version from - https://github.com/iwmc-git/noELLE/releases");
        }

        logger.info("Loading libraries...");
        var commonLoader = new CommonLoader(pluginRoot);
        commonLoader.start();

        var downloaded = commonLoader.downloaded();
        downloaded.forEach(path -> proxyServer.getPluginManager().addToClasspath(this, path));

        var commandManager = proxyServer.getCommandManager();
        commandManager.register("noellev", new MainCommand());

        var eventManager = proxyServer.getEventManager();
        eventManager.fireAndForget(new CoreLoadedEvent(this));
    }

    @Subscribe
    public void onServerStopping(ProxyShutdownEvent event) {
        var formattedMessage = String.format("%s v%s is stopping now...", description.getName().get(), description.getVersion().get());
        logger.info(formattedMessage);

        var eventManager = proxyServer.getEventManager();
        eventManager.fireAndForget(new CoreUnloadedEvent());
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
