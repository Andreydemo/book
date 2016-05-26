package com.epam.cdp.dao;

import com.epam.cdp.RootConfig;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {RootConfig.class})
@ActiveProfiles(profiles = "dev")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void whenGetUserByIdThenUserIsReturned() {
        User user = new UserImpl(3, "test", "test@test.com");
        User result = userDao.getUserById(3);
        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByEmailThenUserIsReturned() {
        User user = new UserImpl(3, "test", "test@test.com");
        assertEquals(user, userDao.getUserByEmail("test@test.com"));
    }

    @Test
    public void whenGetUsersByNameWithOnePageSizeThenListWithOneUserIsReturned() {
        List<Object> users = Arrays.asList(new UserImpl(3, "test", "test@test.com"));
        assertEquals(users, userDao.getUsersByName("test", 1, 1));
    }

    @Test
    public void whenCreateUserThenOneIsCreated() {
        User user = new UserImpl(2, "testUser", "testUser@user.com");
        User result = userDao.createUser(user);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void whenUpdateUserThenOneIsUpdated() {
        User user = new UserImpl(22, "testUser", "testUser@user.com");
        User createdUser = userDao.createUser(user);
        user = new UserImpl(createdUser.getId(), "TEST", "TEST@TEST.com");
        User result = userDao.updateUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenDeleteUSerThenOneIsDeleted() {
        User user = new UserImpl(22, "testUser", "testUser@user.com");
        User createdUser = userDao.createUser(user);
        boolean result = userDao.deleteUser(createdUser.getId());
        assertTrue(result);
    }
}
