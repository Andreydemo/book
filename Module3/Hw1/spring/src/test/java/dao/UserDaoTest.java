package dao;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;
import model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.Storage;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoTest {
    private UserDao userDao;

    @Mock
    Storage storage;

    @Before
    public void setUp() {
        storage = mock(Storage.class);
        userDao = new UserDaoImpl();
        userDao.setStorage(storage);
    }

    @Test
    public void whenGetUserByIdThenUserIsReturned() {
        User user = new UserImpl(1, "user", "user@user.com");
        when(storage.getEntityById(anyString())).thenReturn(user);
        User result = userDao.getUserById(1);
        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByEmailThenUserIsReturned() {
        User user = new UserImpl(1, "user", "user@user.com");
        when(storage.getFirstByPredicate(anyString(), anyObject())).thenReturn(user);
        assertEquals(user, userDao.getUserByEmail("user@user.com"));
    }

    @Test
    public void whenGetUsersByNameWithOnePageSizeThenListWithOneUserIsReturned() {
        List<Object> users = Arrays.asList(new UserImpl(1, "user", "user@user.com"));
        when(storage.getElementsByPredicate(anyString(), anyObject(), anyObject(), anyInt(), anyInt())).thenReturn(users);
        assertEquals(users, userDao.getUsersByName("user", 1, 1));
    }

    @Test
    public void whenCreateUserThenOneIsCreated() {
        User user = new UserImpl(2, "testUser", "testUser@user.com");
        when(storage.put(anyString(), anyObject())).thenReturn(user);
        User result = userDao.createUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenUpdateUserThenOneIsUpdated() {
        User user = new UserImpl(22, "testUser", "testUser@user.com");
        userDao.createUser(user);
        user = new UserImpl(22, "TEST", "TEST@TEST.com");
        when(storage.put(anyString(), eq(user))).thenReturn(user);
        User result = userDao.updateUser(user);
        assertEquals(user, result);
    }

    @Test
    public void whenDeleteUSerThenOneIsDeleted() {
        User user = new UserImpl(22, "testUser", "testUser@user.com");
        when(storage.put(anyString(), anyObject())).thenReturn(user);
        userDao.createUser(user);
        when(storage.delete(anyString())).thenReturn(true);
        boolean result = userDao.deleteUser(user.getId());
        assertTrue(result);
    }
}
