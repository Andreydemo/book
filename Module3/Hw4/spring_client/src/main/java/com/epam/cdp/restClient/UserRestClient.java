package com.epam.cdp.restClient;

import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;

import java.math.BigDecimal;
import java.util.List;

public interface UserRestClient {
    User getUserById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(long userId);

    UserAccount refillAccount(long userId, BigDecimal amount);

    void call();
}
