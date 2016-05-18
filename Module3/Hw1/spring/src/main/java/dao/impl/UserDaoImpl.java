package dao.impl;

import dao.UserDao;
import model.User;
import org.apache.log4j.Logger;
import storage.Storage;

import java.util.List;
import java.util.function.Predicate;

import static storage.NamespaceConstants.USER_NAMESPACE;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
    private Storage storage;

    @Override
    public User getUserById(long id) {
        User user = storage.getEntityById(USER_NAMESPACE + id);
        logger.debug("Returning user by id: " + id + ", " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        Predicate<User> predicate = e -> e.getEmail().equals(email);
        User user = storage.getFirstByPredicate(USER_NAMESPACE, predicate);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    @Override
    public List getUsersByName(String name, int pageSize, int pageNum) {
        Predicate<User> predicate = e -> e.getName().contains(name);
        List<User> users = storage.getElementsByPredicate(USER_NAMESPACE, predicate, (a, b) -> a.getName().compareTo(b.getName()), pageSize, pageNum);
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Override
    public User createUser(User user) {
        logger.debug("Creating user: " + user);
        return storage.put(USER_NAMESPACE + user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        logger.debug("Updating user: " + user);
        return createUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        return storage.delete(USER_NAMESPACE + userId);
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
