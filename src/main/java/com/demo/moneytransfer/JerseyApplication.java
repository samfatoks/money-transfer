package com.demo.moneytransfer;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("services")
public class JerseyApplication extends ResourceConfig {

    private static Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

    public JerseyApplication(Injector injector) {
        packages(JerseyApplication.class.getPackage().toString());
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        register(new ContainerLifecycleListener() {
            public void onStartup(Container container) {
                InjectionManager im = container.getApplicationHandler().getInjectionManager();
                ServiceLocator serviceLocator = im.getInstance(ServiceLocator.class);

                GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);

                guiceBridge.bridgeGuiceInjector(injector);
            }

            public void onReload(Container container) {
            }

            public void onShutdown(Container container) {
                logger.info("Shutting down ");
            }
        });


    }

}


