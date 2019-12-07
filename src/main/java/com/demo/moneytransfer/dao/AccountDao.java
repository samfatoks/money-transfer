package com.demo.moneytransfer.dao;

import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.domain.RequestType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Long create(Account account) throws Exception;
    List<Account> findAll() throws Exception;
    Optional<Account> findByUsername(String username) throws Exception;
}
