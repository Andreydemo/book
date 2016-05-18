package com.epam.cdp.service;

import com.epam.cdp.model.UserAccount;

import java.math.BigDecimal;

public interface UserAccountService {
    void refillAccount(long userId, BigDecimal amount);

    void withdraw(long userId, BigDecimal amount);

    UserAccount create(UserAccount userAccount);

    UserAccount getUserAccountById(long id);

    UserAccount getUserAccountByUserId(long userId);

    boolean deleteUserAccountByUserId(long userId);
}
