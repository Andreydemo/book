package com.epam.cdp.service.impl;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.exception.ApplicationException;
import com.epam.cdp.model.UserAccount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import com.epam.cdp.service.UserAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger logger = Logger.getLogger(UserAccountServiceImpl.class);
    private UserAccountDao userAccountDao;

    @Override
    public void refillAccount(long userId, BigDecimal amount) {
        logger.debug("Refilling users account, userId = " + userId + ", amount = " + amount);
        try {
            userAccountDao.refillAccount(userId, amount);
        } catch (DataAccessException e) {
            logger.error("User's account cannot be refilled, message: " + e.getMessage());
            throw new ApplicationException("User's account cannot be refilled", e);
        }
    }

    @Override
    public void withdraw(long userId, BigDecimal amount) {
        logger.debug("Withdrawing from users account, userId = " + userId + ", amount = " + amount);
        try {
            userAccountDao.withdraw(userId, amount);
        } catch (DataAccessException e) {
            logger.error("User's account cannot be withdrawn from, message: " + e.getMessage());
            throw new ApplicationException("User's account cannot be withdrawn from", e);
        }
    }

    @Override
    public UserAccount create(UserAccount userAccount) {
        logger.debug("Creating userAccount: " + userAccount);
        try {
            return userAccountDao.createUserAccount(userAccount);
        } catch (DataAccessException e) {
            logger.error("User's account cannot be created, message: " + e.getMessage());
            throw new ApplicationException("User's account cannot be created", e);
        }
    }

    @Override
    public UserAccount getUserAccountById(long id) {
        UserAccount userAccount = userAccountDao.getUserAccountById(id);
        logger.debug("Returning userAccount by id: " + id + ", = " + userAccount);
        return userAccount;
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        UserAccount userAccount = userAccountDao.getUserAccountByUserId(userId);
        logger.debug("Returning userAccount by userId: " + userId + ", = " + userAccount);
        return userAccount;
    }

    @Override
    public boolean deleteUserAccountByUserId(long userId) {
        boolean result = userAccountDao.deleteUserAccountByUserId(userId);
        logger.debug("Deleting user account by userId = " + userId + ", result: " + result);
        return result;
    }

    @Autowired
    public void setUserAccountDao(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }
}
