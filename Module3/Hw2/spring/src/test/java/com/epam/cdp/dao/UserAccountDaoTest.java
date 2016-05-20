package com.epam.cdp.dao;

import com.epam.cdp.model.UserAccount;
import com.epam.cdp.model.impl.UserAccountImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/config/context.xml")
@Transactional
public class UserAccountDaoTest {
    @Autowired
    private UserAccountDao userAccountDao;

    @Test
    public void whenGetUserAccountByIdThenUserAccountIsReturned() {
        UserAccount expected = new UserAccountImpl(1, 1, new BigDecimal("0.00"));
        UserAccount result = userAccountDao.getUserAccountById(1);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetUserAccountByUserIdThenUserAccountIsReturned() {
        UserAccount expected = new UserAccountImpl(1, 1, new BigDecimal("0.00"));
        UserAccount result = userAccountDao.getUserAccountByUserId(1);
        assertEquals(expected, result);
    }

    @Test
    public void whenCreateUserAccountThenOneIsCreated() {
        UserAccount userAccount = new UserAccountImpl(1, 1, new BigDecimal("0.00"));
        UserAccount createdUserAccount = userAccountDao.createUserAccount(userAccount);
        assertEquals(userAccount.getUserId(), createdUserAccount.getUserId());
        assertEquals(userAccount.getBalance(), createdUserAccount.getBalance());
    }

    @Test
    public void whenDeleteUserAccountByUserIdTheOneIsDeleted() {
        UserAccount userAccount = userAccountDao.getUserAccountById(1);
        assertNotNull(userAccount);
        boolean result = userAccountDao.deleteUserAccountByUserId(1);
        assertTrue(result);
        userAccount = userAccountDao.getUserAccountById(1);
        assertNull(userAccount);
    }

    @Test
    public void whenRefillAccountThenBalanceIsRefilled() {
        UserAccount userAccount = new UserAccountImpl(1, 1, new BigDecimal("0.00"));
        UserAccount createdUserAccount = userAccountDao.createUserAccount(userAccount);
        BigDecimal amountToRefill = new BigDecimal("300.00");
        userAccountDao.refillAccount(createdUserAccount.getUserId(), amountToRefill);
        UserAccount account = userAccountDao.getUserAccountById(createdUserAccount.getId());
        assertEquals(amountToRefill, account.getBalance());
    }

    @Test
    public void whenWithdrawMoneyFromUserAccountThenMoneyAreWithdrawn() {
        UserAccount userAccount = new UserAccountImpl(1, 1, new BigDecimal("20.00"));
        UserAccount createdUserAccount = userAccountDao.createUserAccount(userAccount);
        userAccountDao.withdraw(createdUserAccount.getUserId(), new BigDecimal("15.00"));
        UserAccount account = userAccountDao.getUserAccountById(createdUserAccount.getId());
        assertEquals(new BigDecimal("5.00"), account.getBalance());
    }
}