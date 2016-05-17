package com.epam.cdp.dao.impl;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.dao.mapper.UserAccountRowMapper;
import com.epam.cdp.model.UserAccount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserAccountDaoImpl implements UserAccountDao {
    private static final Logger logger = Logger.getLogger(UserAccountDaoImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public void refillAccount(long userId, BigDecimal amount) {
        String sql = "Update user_account set balance = balance + ? where userID = ?";
        logger.debug("Refilling users account, userId = " + userId + ", amount = " + amount);
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public UserAccount createUserAccount(UserAccount userAccount) {
        logger.debug("Creating userAccount: " + userAccount);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user_account").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userAccount.getUserId());
        parameters.put("balance", userAccount.getBalance());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return getUserAccountById(key.longValue());
    }

    @Override
    public UserAccount getUserAccountById(long id) {
        String sql = "Select id, userId, balance from user_account where id =?";
        List<UserAccount> userAccounts = jdbcTemplate.query(sql, new Object[]{id}, new UserAccountRowMapper());
        logger.debug("Returning userAccount by id: " + id + ", = " + userAccounts);
        return userAccounts.isEmpty() ? null : userAccounts.get(0);
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        String sql = "Select id, userId, balance from user_account where userId =?";
        List<UserAccount> userAccounts = jdbcTemplate.query(sql, new Object[]{userId}, new UserAccountRowMapper());
        logger.debug("Returning userAccount by userId: " + userId + ", = " + userAccounts);
        return userAccounts.isEmpty() ? null : userAccounts.get(0);
    }

    @Override
    public void withdraw(long userId, BigDecimal amount) {
        String sql = "Update user_account set balance = balance - ? where userID = ?";
        logger.debug("Withdrawing " + amount + " from users account, userId = " + userId);
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public boolean deleteUserAccountByUserId(long userId) {
        String sql = "Delete from user_account where userId = ?";
        logger.debug("Deleting userAccount by userId = " + userId);
        return jdbcTemplate.update(sql, userId) != 0;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
