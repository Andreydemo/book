package com.epam.cdp.dao.mapper;

import com.epam.cdp.model.UserAccount;
import com.epam.cdp.model.impl.UserAccountImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountRowMapper implements RowMapper<UserAccount> {
    @Override
    public UserAccount mapRow(ResultSet resultSet, int i) throws SQLException {
        UserAccount userAccount = new UserAccountImpl();
        userAccount.setId(resultSet.getLong("id"));
        userAccount.setUserId(resultSet.getLong("userId"));
        userAccount.setBalance(resultSet.getBigDecimal("balance"));
        return userAccount;
    }
}
