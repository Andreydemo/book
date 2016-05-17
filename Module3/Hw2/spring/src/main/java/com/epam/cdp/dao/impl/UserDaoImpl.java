package com.epam.cdp.dao.impl;

import com.epam.cdp.dao.UserDao;
import com.epam.cdp.dao.mapper.UserRowMapper;
import com.epam.cdp.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(long id) {
        String sql = "Select * from user where id = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{id}, new UserRowMapper());
        logger.debug("Returning user by id: " + id + ", " + users);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "Select * from user where email = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, new UserRowMapper());
        logger.debug("Returning user by email: " + email + ", " + users);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List getUsersByName(String name, int pageSize, int pageNum) {
        String sql = "Select * from user where name = ? limit ?,?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{name, pageSize * pageNum - pageSize, pageSize}, new UserRowMapper());
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return getUserById(key.longValue());
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        String sql = "Update user set name = ?, email = ? where id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        String sql = "Delete from user where id = ?";
        return jdbcTemplate.update(sql, userId) != 0;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
