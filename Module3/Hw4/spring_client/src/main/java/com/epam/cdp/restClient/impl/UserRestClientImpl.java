package com.epam.cdp.restClient.impl;

import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;
import com.epam.cdp.model.impl.UserAccountImpl;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.restClient.UserRestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.cdp.restClient.impl.Constants.EMAIL;
import static com.epam.cdp.restClient.impl.Constants.NAME;
import static com.epam.cdp.restClient.impl.Constants.PAGE_SIZE_PAGE_NUM;

@Component
public class UserRestClientImpl implements UserRestClient {
    private static final Logger logger = Logger.getLogger(UserRestClientImpl.class);
    private RestTemplate restTemplate;
    private String baseUrl;

    @Autowired
    public UserRestClientImpl(RestTemplate restTemplate, @Value("${url.users}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public User getUserById(long id) {
        User user = restTemplate.getForObject(baseUrl + "{id}", UserImpl.class, id);
        logger.debug("Returning user by id: " + id + ", " + user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = restTemplate.getForObject(baseUrl + EMAIL, UserImpl.class, email);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> users = restTemplate.getForObject(baseUrl + NAME + PAGE_SIZE_PAGE_NUM, List.class, name, pageSize, pageNum);
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    @Override
    public User createUser(User user) {
        ResponseEntity<UserImpl> entity = restTemplate.postForEntity(baseUrl, user, UserImpl.class);
        logger.debug("Creating user: " + user);
        return entity.getBody();
    }

    @Override
    public void updateUser(User user) {
        logger.debug("Updating user: " + user);
        restTemplate.put(baseUrl, user, UserImpl.class);
    }

    @Override
    public void deleteUser(long userId) {
        logger.debug("Deleting user by id " + userId);
        restTemplate.delete(baseUrl + "{id}", userId);
    }

    @Override
    public UserAccount refillAccount(long userId, BigDecimal amount) {
        ResponseEntity<UserAccountImpl> entity = restTemplate.postForEntity(baseUrl + "{id}/balance", amount, UserAccountImpl.class, userId);
        return entity.getBody();
    }

    @Override
    public void call() {
        logger.debug(createUser(new UserImpl("name", "email@email")));
        User user = getUserByEmail("email@email");
        logger.debug(user);
        logger.debug(getUserById(user.getId()));
        logger.debug(getUsersByName(user.getName(), 10, 1));
        updateUser(new UserImpl(user.getId(), "nameEE", "lllll"));
        deleteUser(user.getId());
    }
}