package com.epam.cdp.dao;

import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;

import java.util.List;

public interface UserDao {
    User getUserById(long id);

    User getUserByEmail(String email);

    List getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    void insertBatch(List<UserImpl> users);
}
