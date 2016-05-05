package service;

import dao.UserDao;
import model.User;

import java.util.List;

public interface UserService {
    void setUserDao(UserDao userDao);

    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);
}
