package com.epam.cdp.service;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.model.User;
import com.epam.cdp.repository.UserRepository;

import java.util.List;

public interface UserService {
    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    void setUserAccountDao(UserAccountDao userAccountDao);

    void setUserRepository(UserRepository userRepository);
}
