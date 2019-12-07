package com.demo.moneytransfer;

import com.demo.moneytransfer.di.module.CentralModule;
import com.demo.moneytransfer.di.module.ConfigModule;
import com.demo.moneytransfer.server.JettyFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;

public class Application {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CentralModule(), new ConfigModule());
        JettyFactory jettyFactory = new JettyFactory(injector);
        Server server = jettyFactory.createServer();
        jettyFactory.startAndWait(server);
    }
}
