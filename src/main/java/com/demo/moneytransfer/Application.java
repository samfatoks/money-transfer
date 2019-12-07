package com.demo.moneytransfer;

import com.demo.moneytransfer.di.module.CentralModule;
import com.demo.moneytransfer.di.module.ConfigModule;
import com.demo.moneytransfer.server.JettyFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CentralModule(), new ConfigModule());
        JettyFactory jettyFactory = new JettyFactory(injector);
        Server server = jettyFactory.createServer();
        try {
            server.start();
            logger.info("Jetty started at: {}, {}", server.getURI(), "Press Ctrl+C to shut down.");
            server.join();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start server: ", e);
        }
    }
}
