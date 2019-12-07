package com.demo.moneytransfer.di.module;

import com.demo.moneytransfer.dao.DBHelper;
import com.google.inject.AbstractModule;

public class CentralModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DBHelper.class).asEagerSingleton();
    }
}
