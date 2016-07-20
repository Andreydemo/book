package batulin.service;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotCreatedException;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.exception.UsersAreNotFriendsException;
import com.epam.cdp.batulin.repository.UserRepository;
import com.epam.cdp.batulin.service.impl.UserServiceImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl();
        userService.setUserRepository(userRepository);
    }

    @Test
    public void whenCreateUserTheOneIsCreated() {
        User user = new User("username", "name", new Date(1));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getDateOfBirth(), createdUser.getDateOfBirth());
    }

    @Test
    public void whenCreateUserButUsernameIsTakenThenUserNotCreatedIsThrown() {
        exception.expect(UserNotCreatedException.class);
        exception.expectMessage("User with such username already exists");
        User user = new User("username", "name", new Date(1));
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        userService.createUser(new User());
    }

    @Test
    public void whenFindUserByUsernameThenOneIsReturned() {
        User user = new User("username", "name", new Date(1));
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        User foundUser = userService.getUserByUsername(user.getUsername());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getDateOfBirth(), foundUser.getDateOfBirth());
    }

    @Test
    public void whenFindUserNonExistingThenUserNotFoundExceptionIsThrown() {
        String username = "username";
        exception.expect(UserNotFoundException.class);
        exception.expectMessage("user with username = " + username + " cannot be found");
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.getUserByUsername(username);
    }

    @Test
    public void whenAddUserToFriendListThenFriendIsAdded() {
        User user = new User("username", "name", new Date(1));
        User friend = new User("friend", "friend", new Date(2));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.findByUsername(friend.getUsername())).thenReturn(friend);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.addUserToFriendList(user.getUsername(), friend.getUsername());
        assertTrue(updatedUser.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(updatedUser));
    }

    @Test
    public void whenAddNonExistingUserToFriendListThenUserNotFoundExceptionIsThrown() {
        String username = "username";
        exception.expect(UserNotFoundException.class);
        exception.expectMessage("user with username = " + username + " cannot be found");
        when(userRepository.findByUsername(username)).thenReturn(null);

        userService.addUserToFriendList(username, "");
    }

    @Test
    public void whenAddUserToFriendListOfNonExistingUserThenUserNotFoundExceptionIsThrown() {
        String username = "username";
        exception.expect(UserNotFoundException.class);
        exception.expectMessage("user with username = " + username + " cannot be found");
        when(userRepository.findByUsername("")).thenReturn(new User());
        when(userRepository.findByUsername(username)).thenReturn(null);

        userService.addUserToFriendList("", username);
    }

    @Test
    public void whenUsersAreNotFriendsTheUsersAreNotFriendsExceptionIsThrown() {
        User user = new User("username", "name", new Date(1));
        User friend = new User("friend", "name", new Date(1));
        exception.expect(UsersAreNotFriendsException.class);
        exception.expectMessage("Users " + user.getUsername() + " and " + friend.getUsername() + " are not friends, throwing UsersAreNotFriendsException");

        userService.checkIfUsersAreFriends(user, friend);
    }
}