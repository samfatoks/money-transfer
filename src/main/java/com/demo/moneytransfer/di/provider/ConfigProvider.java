package com.demo.moneytransfer.di.provider;

import com.demo.moneytransfer.config.Configuration;
import com.demo.moneytransfer.config.ConfigManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ConfigProvider implements Provider<Configuration> {

    @Inject
    private ConfigManager configManager;

    @Override
    public Configuration get() {
        return configManager.getConfiguration();
    }
}
