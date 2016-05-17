package com.epam.cdp.service.impl;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.exception.ApplicationException;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserAccountImpl;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.repository.UserRepository;
import com.epam.cdp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private UserAccountDao userAccountDao;

    @Override
    public User getUserById(long id) {
        System.out.println(userAccountDao);
        User user = userRepository.findOne(id);
        logger.debug("Returning user by id: " + id + ", " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> users = userRepository.findByName(name, new PageRequest(pageNum - 1, pageSize));
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        try {
            User createdUser = userRepository.save((UserImpl) user);
            userAccountDao.createUserAccount(new UserAccountImpl(createdUser.getId(), new BigDecimal("0.00")));
            return createdUser;
        } catch (DataAccessException e) {
            logger.error("User cannot be created, message: " + e.getMessage());
            throw new ApplicationException("User cannot be created", e);
        }
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        try {
            UserImpl userToUpdate = userRepository.findOne(user.getId());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setName(user.getName());
            return userRepository.save(userToUpdate);
        } catch (DataAccessException e) {
            logger.error("User cannot be updated, message: " + e.getMessage());
            throw new ApplicationException("User cannot be updated", e);
        }
    }

    @Transactional
    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        userAccountDao.deleteUserAccountByUserId(userId);
        userRepository.delete(userId);
        return userRepository.findOne(userId) == null;
    }

    @Override
    @Autowired
    public void setUserAccountDao(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    @Override
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
