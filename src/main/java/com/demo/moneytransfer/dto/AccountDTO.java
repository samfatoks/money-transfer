package com.demo.moneytransfer.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class AccountDTO {
    private Long id;
    @NotNull
    private String username;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
