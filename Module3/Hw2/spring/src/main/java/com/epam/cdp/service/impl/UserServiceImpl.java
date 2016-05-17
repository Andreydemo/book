package com.epam.cdp.service.impl;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.dao.UserDao;
import com.epam.cdp.exception.ApplicationException;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserAccountImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.cdp.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private UserDao userDao;
    private UserAccountDao userAccountDao;

    @Override
    public User getUserById(long id) {
        User user = userDao.getUserById(id);
        logger.debug("Returning user by id: " + id + ", " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> users = userDao.getUsersByName(name, pageSize, pageNum);
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        try {
            User createdUser = userDao.createUser(user);
            userAccountDao.createUserAccount(new UserAccountImpl(createdUser.getId(), new BigDecimal("0.00")));
            return createdUser;
        } catch (DataAccessException e) {
            logger.error("User cannot be created, message: " + e.getMessage());
            throw new ApplicationException("User cannot be created", e);
        }
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        try {
            return userDao.updateUser(user);
        } catch (DataAccessException e) {
            logger.error("User cannot be updated, message: " + e.getMessage());
            throw new ApplicationException("User cannot be updated", e);
        }
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        userAccountDao.deleteUserAccountByUserId(userId);
        return userDao.deleteUser(userId);
    }

    @Autowired
    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    @Override
    public void setUserAccountDao(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }
}
