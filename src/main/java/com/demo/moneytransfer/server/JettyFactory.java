package com.demo.moneytransfer.server;

import com.demo.moneytransfer.JerseyApplication;
import com.demo.moneytransfer.config.Configuration;
import com.demo.moneytransfer.config.JettyConfig;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyFactory {

    private static final boolean DAEMON = true;
    private static final String DAEMON_NAME = "MONEY_TRANSFER";

    private Injector injector;
    private JettyConfig jettyConfig;

    public JettyFactory(Injector injector) {
        this.injector = injector;
        this.jettyConfig = injector.getInstance(Configuration.class).getServer();
    }

    public Server createServer() {

        // Create server
        QueuedThreadPool threadPool = new QueuedThreadPool(jettyConfig.getMaxThreads(), jettyConfig.getMinThreads());
        threadPool.setDaemon(DAEMON);
        threadPool.setName(DAEMON_NAME);

        final Server server = new Server(threadPool);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(new JerseyApplication(injector)));
        contextHandler.addServlet(servletHolder, "/*");
        contextHandler.setContextPath("/");



        // Handlers
        final HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(contextHandler);
        server.setHandler(handlers);

        // Connector
        final ServerConnector connector = new ServerConnector(server);
        connector.setHost(jettyConfig.getHost());
        connector.setPort(jettyConfig.getPort());
        connector.setIdleTimeout(jettyConfig.getIdleTimeout());
        server.addConnector(connector);

        server.setStopAtShutdown(true);
        server.setStopTimeout(5000);

        return server;
    }
}
