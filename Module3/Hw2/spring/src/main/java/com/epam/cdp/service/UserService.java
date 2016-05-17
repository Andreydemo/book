package com.epam.cdp.service;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.dao.UserDao;
import com.epam.cdp.model.User;

import java.util.List;

public interface UserService {
    void setUserDao(UserDao userDao);

    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    void setUserAccountDao(UserAccountDao userAccountDao);
}
