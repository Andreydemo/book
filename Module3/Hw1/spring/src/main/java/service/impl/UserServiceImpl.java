package service.impl;

import dao.UserDao;
import exception.ApplicationException;
import model.User;
import org.apache.log4j.Logger;
import service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private UserDao userDao;

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

    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        checkIfEmailTaken(user.getEmail());
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        checkIfEmailTaken(user.getEmail());
        return userDao.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        return userDao.deleteUser(userId);
    }

    private void checkIfEmailTaken(String email) {
        if (userDao.getUserByEmail(email) != null) {
            logger.error("User with email " + email + " is taken. Throwing Application Exception");
            throw new ApplicationException("User with such email already exists");
        }
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
