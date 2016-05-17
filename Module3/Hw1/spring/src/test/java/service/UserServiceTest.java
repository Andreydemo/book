package service;

import dao.UserDao;
import exception.ApplicationException;
import model.User;
import model.impl.UserImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import service.impl.UserServiceImpl;

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
    UserDao userDao;

    @Before
    public void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserServiceImpl();
        userService.setUserDao(userDao);
    }

    @Test
    public void whenGetUserByIdThenUserIsReturned() {
        User user = new UserImpl(1, "user", "user@user.com");
        when(userDao.getUserById(anyLong())).thenReturn(user);
        User result = userService.getUserById(1);
        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByEmailThenUserIsReturned() {
        User user = new UserImpl(1, "user", "user@user.com");
        when(userDao.getUserByEmail(anyString())).thenReturn(user);
        assertEquals(user, userService.getUserByEmail("user@user.com"));
    }

    @Test
    public void whenGetUsersByNameThenListOfUsersIsReturned() {
        List<Object> users = Arrays.asList(new UserImpl(1, "user", "user@user.com"));
        when(userDao.getUsersByName(anyString(), anyInt(), anyInt())).thenReturn(users);
        assertEquals(users, userService.getUsersByName("user", 1, 1));
    }

    @Test
    public void whenCreateUserThenOneIsCreated() {
        User user = new UserImpl(2, "testUser", "testUser@user.com");
        when(userDao.createUser(anyObject())).thenReturn(user);
        User result = userService.createUser(user);
        assertEquals(user, result);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void whenCreateUserWithExistingEmailThenApplicationExceptionIsThrown() {
        exception.expect(ApplicationException.class);
        exception.expectMessage("User with such email already exists");
        User user = new UserImpl(2, "testUser", "testUser@user.com");
        when(userDao.getUserByEmail(anyString())).thenReturn(user);
        userService.createUser(user);
    }

    @Test
    public void whenUpdateUserThenOneIsUpdated() {
        User user = new UserImpl(22, "testUser", "testUser@user.com");
        when(userDao.updateUser(eq(user))).thenReturn(user);
        User result = userService.updateUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenDeleteUserThenOneIsDeleted() {
        when(userDao.deleteUser(anyLong())).thenReturn(true);
        boolean result = userService.deleteUser(1);
        assertTrue(result);
    }
}
