package com.demo.moneytransfer.di.module;

import com.demo.moneytransfer.config.Configuration;
import com.demo.moneytransfer.config.ConfigManagerImpl;
import com.demo.moneytransfer.config.ConfigManager;
import com.demo.moneytransfer.di.provider.ConfigProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConfigManager.class).to(ConfigManagerImpl.class).in(Singleton.class);
        bind(Configuration.class).toProvider(ConfigProvider.class);
    }
}
