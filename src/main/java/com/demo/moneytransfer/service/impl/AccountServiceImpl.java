package com.demo.moneytransfer.service.impl;

import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.domain.RequestType;
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

    @Override
    public boolean debitAccount(String username, BigDecimal amount) throws Exception {
        int updateCount = accountDao.updateBalance(username, amount, RequestType.DEBIT);
        return updateCount == 1;
    }

    @Override
    public boolean creditAccount(String username, BigDecimal amount) throws Exception {
        int updateCount = accountDao.updateBalance(username, amount, RequestType.CREDIT);
        return updateCount == 1;
    }


    @Override
    public boolean transfer(String srcUsername, String destUsername, BigDecimal amount) throws Exception {
        int updateCount = accountDao.transferMoney(srcUsername, destUsername, amount);
        return updateCount == 2;
    }

    @Override
    public void deleteAccount(String username) throws Exception {
        accountDao.delete(username);
    }
}
