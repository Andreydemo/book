package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;

import static com.epam.cdp.batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.convertObjectToJsonBytes;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.createExceptionResolver;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    @Mock
    UserService userService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        UserController userController = new UserController();
        userController.setUserService(userService);
        mvc = MockMvcBuilders.standaloneSetup(userController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenCreteUserThenUserIsCreated() throws Exception {
        String username = "max";
        User user = new User(username, "xxx", new Date(1));
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.username", is(user.getUsername()))).
                andExpect(jsonPath("$.name", is(user.getName()))).
                andExpect(jsonPath("$.dateOfBirth", is(user.getDateOfBirth().toString())));

        verify(userService, times(1)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenCreteUserWithNotValidUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = "x";
        User user = new User(username, "xxx", new Date(1));
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username must be from 3 to 128 symbols")));

        verify(userService, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenCreteUserWithNotValidNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("username", "n", new Date(1));
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name must be from 3 to 128 symbols")));

        verify(userService, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenCreteUserWithNullUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = null;
        User user = new User(username, "xxx", new Date(1));
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username cannot be null")));

        verify(userService, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenCreteUserWithNullNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", null, new Date(1));
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name cannot be null")));

        verify(userService, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenCreteUserWithNullDateOfBirthThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", "xxx", null);
        when(userService.createUser(anyObject())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: dateOfBirth cannot be null")));

        verify(userService, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenAddUserToFriendsThenFriendIsAdded() throws Exception {
        String username = "username";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.getFriends().add(friend);
        when(userService.addUserToFriendList(anyString(), anyString())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/user/{name}/friend", username).
                content(convertObjectToJsonBytes(friend)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].name", is(friend.getName()))).
                andExpect(jsonPath("$[0].username", is(friend.getUsername())));

        verify(userService, times(1)).addUserToFriendList(anyString(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenAddUserToFriendsWithNonExistingUserThenResponseStatusIsNotFound() throws Exception {
        String username = "username";
        User friend = new User("friend", "name");
        when(userService.addUserToFriendList(username, friend.getUsername())).thenThrow(new UserNotFoundException());

        mvc.perform(MockMvcRequestBuilders.post("/user/{name}/friend", username).
                content(convertObjectToJsonBytes(friend)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));

        verify(userService, times(1)).addUserToFriendList(anyString(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenGetFriendsThenFriendsAreReturned() throws Exception {
        String username = "max";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.setFriends(asList(friend));
        when(userService.getUserByUsername(username)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].name", is(friend.getName()))).
                andExpect(jsonPath("$[0].username", is(friend.getUsername())));

        verify(userService, times(1)).getUserByUsername(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void whenGetFriendsForNonExistingUserThenResponseStatusIsNotFound() throws Exception {
        String username = "max";

        when(userService.getUserByUsername(username)).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));

        verify(userService, times(1)).getUserByUsername(anyString());
        verifyNoMoreInteractions(userService);
    }

}
