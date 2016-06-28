package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.handler.CommandHandler;
import com.epam.cdp.batulin.entity.User;

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
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserCommandControllerTest {
    @Mock
    CommandHandler commandHandler;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        commandHandler = mock(CommandHandler.class);
        UserCommandController userController = new UserCommandController();
        userController.setCommandHandler(commandHandler);
        mvc = MockMvcBuilders.standaloneSetup(userController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenCreteUserThenUserIsCreated() throws Exception {
        String username = "max";
        User user = new User(username, "xxx", new Date(1));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        verify(commandHandler, times(1)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenCreteUserWithNotValidUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = "x";
        User user = new User(username, "xxx", new Date(1));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username must be from 3 to 128 symbols")));

        verify(commandHandler, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenCreteUserWithNotValidNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("username", "n", new Date(1));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name must be from 3 to 128 symbols")));

        verify(commandHandler, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenCreteUserWithNullUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = null;
        User user = new User(username, "xxx", new Date(1));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username cannot be null")));

        verify(commandHandler, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenCreteUserWithNullNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", null, new Date(1));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name cannot be null")));

        verify(commandHandler, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenCreteUserWithNullDateOfBirthThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", "xxx", null);

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: dateOfBirth cannot be null")));

        verify(commandHandler, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenAddUserToFriendsThenFriendIsAdded() throws Exception {
        String username = "username";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.getFriends().add(friend);

        mvc.perform(MockMvcRequestBuilders.post("/user/{name}/friend", username).
                content(convertObjectToJsonBytes(friend)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        verify(commandHandler, times(1)).addFriend(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenUpdateUserThenUserIsUpdated() throws Exception {
        String username = "max";
        String name = "name";
        Date date = new Date(1);
        User user = new User(null, name, date);

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());

        verify(commandHandler, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenUpdateUserWithNotValidNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = "max";
        String name = "1";
        Date date = new Date(1);
        User user = new User(null, name, date);

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be updated: name must be from 3 to 128 symbols")));

        verify(commandHandler, times(0)).updateUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenUpdateUserWithNullNameAndNewDateOfBirthThenUserIsUpdated() throws Exception {
        String username = "max";
        Date date = new Date(1);
        String validName = "validName";

        User user = new User(null, null, date);

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());

        verify(commandHandler, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenUpdateUserWithNullDateOfBirthAndNewNameThenUserIsUpdated() throws Exception {
        String username = "max";
        String name = "name";
        Date date = new Date(1);

        User user = new User(null, name, null);

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());

        verify(commandHandler, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }
}