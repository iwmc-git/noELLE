package noelle.loaders.velocity;

import com.google.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

public class VelocityLoader {
    private final ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public VelocityLoader(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Subscribe
    public void onServerStartup(ProxyInitializeEvent event) {

    }
}
