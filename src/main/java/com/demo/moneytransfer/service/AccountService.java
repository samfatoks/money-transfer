package com.demo.moneytransfer.service;

import com.demo.moneytransfer.domain.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Long createAccount(Account account) throws Exception;
    List<Account> findAllAccounts() throws Exception;
    Optional<Account> findAccountByUsername(String username) throws Exception;
    boolean debitAccount(String username, BigDecimal amount) throws Exception;
    boolean creditAccount(String username, BigDecimal amount) throws Exception;
    boolean transfer(String srcUsername, String destUsername, BigDecimal amount) throws Exception;
    void deleteAccount(String username) throws Exception;
}
