package noelle.loaders.velocity;

import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import noelle.loaders.common.CommonLoader;
import noelle.loaders.velocity.commands.MainCommand;

import org.slf4j.Logger;

import java.nio.file.Path;

public final class VelocityLoader {
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

        logger.info("Loading libraries...");
        var commonLoader = new CommonLoader(pluginRoot);
        commonLoader.start();

        logger.info("Injecting libraries...");
        var downloaded = commonLoader.downloaded();
        downloaded.forEach(path -> proxyServer.getPluginManager().addToClasspath(this, path));

        logger.info("Registering commands...");
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
