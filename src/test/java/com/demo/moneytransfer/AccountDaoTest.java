package com.demo.moneytransfer;


import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.di.module.CentralModule;
import com.demo.moneytransfer.di.module.ConfigModule;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.domain.RequestType;
import com.demo.moneytransfer.exception.AppException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDaoTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private AccountDao accountDao;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new CentralModule(), new ConfigModule());
        accountDao = injector.getInstance(AccountDao.class);
    }

    @Test
    public void given_valid_account_dao_create_should_persist_account() throws Exception {

        Account account = new Account("user1");
        accountDao.create(account);

        final Account accountRetrieved = accountDao.findByUsername(account.getUsername()).get();
        assertThat(accountRetrieved.getUsername()).isEqualTo(account.getUsername());
    }

    @Test
    public void given_invalid_account_dao_create_should_throw_exception() throws Exception {
        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("Unable to create account");

        Account account = new Account();
        Long id = accountDao.create(account);
    }

    @Test
    public void given_valid_account_dao_findbyusername_should_return_valid_result() throws Exception {
        Optional<Account> accountOpt = accountDao.findByUsername("john");
        assertThat(accountOpt.isPresent()).isTrue();
        assertThat(accountOpt.get().getUsername()).isEqualTo("john");
    }

    @Test
    public void given_invalid_account_dao_findbyusername_should_return_empty_optional() throws Exception {
        Account account = new Account("user1");
        Optional<Account> accountOpt = accountDao.findByUsername(account.getUsername());
        assertThat(accountOpt.isPresent()).isFalse();
    }

    @Test
    public void given_valid_account_and_amount_dao_updatebalance_should_increase_balance_by_amount() throws Exception {

        Account account = new Account("user1");
        accountDao.create(account);

        accountDao.updateBalance(account.getUsername(), new BigDecimal(50), RequestType.CREDIT);

        final Account accountRetrieved = accountDao.findByUsername(account.getUsername()).get();

        assertThat(accountRetrieved.getBalance().intValue()).isEqualTo(50);
    }

    @Test
    public void given_valid_accounts_dao_tranfer_with_amount_above_balance_should_throw_exception() throws Exception {

        expectedEx.expect(AppException.class);
        expectedEx.expectMessage("Insufficient balance");

        Account account1 = new Account("fred", new BigDecimal(300));
        Account account2 = new Account("prince", new BigDecimal(50));
        accountDao.create(account1);
        accountDao.create(account2);

        BigDecimal transferAmount = new BigDecimal(1000);

        int updateCount = accountDao.transferMoney(account1.getUsername(), account2.getUsername(), transferAmount);
    }

    @Test
    public void given_valid_accounts_balance_after_tranfer_should_be_expected_value() throws Exception {
        Account account1 = new Account("fred", new BigDecimal(300));
        Account account2 = new Account("prince", new BigDecimal(50));
        accountDao.create(account1);
        accountDao.create(account2);

        BigDecimal transferAmount = new BigDecimal(100);
        //Transfer 100 USD from account1 to account2
        int updateCount = accountDao.transferMoney(account1.getUsername(), account2.getUsername(), transferAmount);
        final Account account1Retrieved = accountDao.findByUsername(account1.getUsername()).get();
        final Account account2Retrieved = accountDao.findByUsername(account2.getUsername()).get();

        assertThat(updateCount).isEqualTo(2);
        BigDecimal account1ExpectedBalance = account1.getBalance().subtract(transferAmount);
        BigDecimal account2ExpectedBalance = account2.getBalance().add(transferAmount);
        assertThat(account1Retrieved.getBalance().intValue()).isEqualTo(account1ExpectedBalance.intValue());
        assertThat(account2Retrieved.getBalance().intValue()).isEqualTo(account2ExpectedBalance.intValue());
    }

    @Test
    public void given_valid_account_dao_delete_should_remove_account() throws Exception {
        Account account = new Account("fred", new BigDecimal(300));
        accountDao.create(account);
        Optional<Account> accountOpt  = accountDao.findByUsername(account.getUsername());
        assertThat(accountOpt.isPresent()).isTrue();

        accountDao.delete(account.getUsername());
        Optional<Account> accountOpt2  = accountDao.findByUsername(account.getUsername());
        assertThat(accountOpt2.isPresent()).isFalse();
    }
}