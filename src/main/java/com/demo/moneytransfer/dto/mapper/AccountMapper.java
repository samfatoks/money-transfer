package com.demo.moneytransfer.dto.mapper;

import com.demo.moneytransfer.dto.AccountDTO;
import com.demo.moneytransfer.domain.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {
    public static AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setBalance(account.getBalance());
        return accountDTO;
    }

    public static List<AccountDTO> toDTOList(List<Account> accounts) {
        return accounts.stream().map(AccountMapper::toDTO).collect(Collectors.toList());
    }

    public static Account fromDTO(AccountDTO accountDTO) {
        return new Account(accountDTO.getUsername());
    }
}
