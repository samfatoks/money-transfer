package com.demo.moneytransfer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Long id;
    private String username;
    private BigDecimal balance;

    public Account() {
    }

    public Account(String username) {
        this.username = username;
        this.balance = new BigDecimal(0);
    }
    public Account(Long id, String username, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Account{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId()) &&
                Objects.equals(getUsername(), account.getUsername()) &&
                Objects.equals(getBalance(), account.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getBalance());
    }

}