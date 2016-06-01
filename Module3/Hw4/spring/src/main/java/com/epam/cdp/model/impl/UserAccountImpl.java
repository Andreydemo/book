package com.epam.cdp.model.impl;

import com.epam.cdp.model.UserAccount;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class UserAccountImpl implements UserAccount, Serializable {
    private long id;
    private long userId;
    private BigDecimal balance;

    public UserAccountImpl() {
    }

    public UserAccountImpl(long id, long userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public UserAccountImpl(long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserAccountImpl{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountImpl that = (UserAccountImpl) o;
        return id == that.id &&
                userId == that.userId &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, balance);
    }
}
