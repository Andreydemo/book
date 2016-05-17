package com.epam.cdp.dao.mapper;

import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new UserImpl();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setName(resultSet.getString("name"));
        return user;
    }
}
