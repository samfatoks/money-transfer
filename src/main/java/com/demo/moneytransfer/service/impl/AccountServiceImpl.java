package com.demo.moneytransfer.service.impl;

import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.domain.RequestType;
import com.demo.moneytransfer.exception.AppException;
import com.demo.moneytransfer.service.AccountService;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    public Long createAccount(Account account) throws Exception  {
        return accountDao.create(account);
    }

    @Override
    public List<Account> findAllAccounts() throws Exception {
        return accountDao.findAll();
    }

    @Override
    public Optional<Account> findAccountByUsername(String username) throws Exception {
        return accountDao.findByUsername(username);
    }

    private Account getAccount(String username) throws Exception {
        return accountDao.findByUsername(username)
                .orElseThrow(() -> new AppException(404, 5, "Invalid account - " + username));

    }
}
