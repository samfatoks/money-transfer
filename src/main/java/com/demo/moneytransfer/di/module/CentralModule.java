package com.demo.moneytransfer.di.module;

import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.dao.DBHelper;
import com.demo.moneytransfer.dao.impl.AccountDaoImpl;
import com.demo.moneytransfer.service.AccountService;
import com.demo.moneytransfer.service.impl.AccountServiceImpl;
import com.google.inject.AbstractModule;

public class CentralModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountDao.class).to(AccountDaoImpl.class);
        bind(DBHelper.class).asEagerSingleton();
    }
}
