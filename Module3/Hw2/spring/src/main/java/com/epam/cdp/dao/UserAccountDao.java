package com.epam.cdp.dao;

import com.epam.cdp.model.UserAccount;

import java.math.BigDecimal;

public interface UserAccountDao {
    void refillAccount(long userId, BigDecimal amount);

    UserAccount createUserAccount(UserAccount userAccount);

    UserAccount getUserAccountById(long id);

    UserAccount getUserAccountByUserId(long userId);

    void withdraw(long userId, BigDecimal amount);

    boolean deleteUserAccountByUserId(long userId);
}
