package com.epam.cdp.service;

import com.epam.cdp.dao.UserAccountDao;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.repository.UserRepository;
import com.epam.cdp.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserAccountDao userAccountDao;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userAccountDao = mock(UserAccountDao.class);
        userService = new UserServiceImpl();
        userService.setUserAccountDao(userAccountDao);
        userService.setUserRepository(userRepository);
    }

    @Test
    public void whenGetUserByIdThenUserIsReturned() {
        UserImpl user = new UserImpl(1, "user", "user@user.com");
        when(userRepository.findOne(anyLong())).thenReturn(user);
        User result = userService.getUserById(1);
        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByEmailThenUserIsReturned() {
        User user = new UserImpl(1, "user", "user@user.com");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        assertEquals(user, userService.getUserByEmail("user@user.com"));
    }

    @Test
    public void whenGetUsersByNameThenListOfUsersIsReturned() {
        List<User> users = Arrays.asList(new UserImpl(1, "user", "user@user.com"));
        when(userRepository.findByName(anyString(), anyObject())).thenReturn(users);
        assertEquals(users, userService.getUsersByName("user", 1, 1));
    }

    @Test
    public void whenCreateUserThenOneIsCreated() {
        UserImpl user = new UserImpl(2, "testUser", "testUser@user.com");
        when(userRepository.save(eq(user))).thenReturn(user);
        User result = userService.createUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenUpdateUserThenOneIsUpdated() {
        UserImpl user = new UserImpl(22, "testUser", "testUser@user.com");
        when(userRepository.save(eq(user))).thenReturn(user);
        when(userRepository.findOne(anyLong())).thenReturn(user);
        User result = userService.updateUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenDeleteUserThenOneIsDeleted() {
        when(userRepository.findOne(anyLong())).thenReturn(null);
        boolean result = userService.deleteUser(1);
        assertTrue(result);
    }
}