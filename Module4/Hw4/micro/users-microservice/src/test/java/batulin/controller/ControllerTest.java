package batulin.controller;

import com.epam.cdp.batulin.controller.Controller;
import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotFoundException;
import com.epam.cdp.batulin.facade.UserFacade;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static batulin.controller.ControllerTestUtils.convertObjectToJsonBytes;
import static batulin.controller.ControllerTestUtils.createExceptionResolver;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest {
    @Mock
    UserFacade userFacade;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        userFacade = mock(UserFacade.class);
        Controller TimelineController = new Controller();
        TimelineController.setUserFacade(userFacade);
        mvc = MockMvcBuilders.standaloneSetup(TimelineController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenAddNoteToUsersTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        when(userFacade.addNoteToUsersTimeline(anyObject())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.noteText", is(note.getNoteText())));

        verify(userFacade, times(1)).addNoteToUsersTimeline(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenGetNotesOnFriendsTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");
        List<Note> list = Arrays.asList(note, new Note(""));

        when(userFacade.getUsersFriendNotes(anyString(), anyString())).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(userFacade, times(1)).getUsersFriendNotes(anyString(), anyString());
    }

    @Test
    public void whenAddNoteToFriendsTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");
        when(userFacade.addNoteToFriendsTimeline(anyString(), anyObject())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.noteText", is(note.getNoteText())));

        verify(userFacade, times(1)).addNoteToFriendsTimeline(anyString(), anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserThenUserIsCreated() throws Exception {
        String username = "max";
        User user = new User(username, "xxx", new Date(1));
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.username", is(user.getUsername()))).
                andExpect(jsonPath("$.name", is(user.getName()))).
                andExpect(jsonPath("$.dateOfBirth", is(user.getDateOfBirth().toString())));

        verify(userFacade, times(1)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserWithNotValidUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        String username = "x";
        User user = new User(username, "xxx", new Date(1));
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username must be from 3 to 128 symbols")));

        verify(userFacade, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserWithNotValidNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("username", "n", new Date(1));
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name must be from 3 to 128 symbols")));

        verify(userFacade, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserWithNullUsernameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User(null, "xxx", new Date(1));
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: username cannot be null")));

        verify(userFacade, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserWithNullNameThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", null, new Date(1));
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: name cannot be null")));

        verify(userFacade, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenCreteUserWithNullDateOfBirthThenErrorWithMessageAndBadRequestStatusCodeWouldBeSent() throws Exception {
        User user = new User("xxx", "xxx", null);
        user.setPassword("password");
        when(userFacade.createUser(anyObject())).thenReturn(new UserDto(user.getName(), user.getUsername(), user.getDateOfBirth()));

        mvc.perform(MockMvcRequestBuilders.post("/createUser").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("User cannot be created: dateOfBirth cannot be null")));

        verify(userFacade, times(0)).createUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenAddUserToFriendsThenFriendIsAdded() throws Exception {
        String username = "username";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.getFriends().add(friend);
        when(userFacade.addToFriendList(anyString(), anyObject())).thenReturn(asList(new UserDto(friend.getName(), friend.getUsername(), friend.getDateOfBirth())));

        mvc.perform(MockMvcRequestBuilders.post("/user/{name}/friend", username).
                content(convertObjectToJsonBytes(friend)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].name", is(friend.getName()))).
                andExpect(jsonPath("$[0].username", is(friend.getUsername())));

        verify(userFacade, times(1)).addToFriendList(anyString(), anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenAddUserToFriendsWithNonExistingUserThenResponseStatusIsNotFound() throws Exception {
        String username = "username";
        User friend = new User("friend", "name");
        when(userFacade.addToFriendList(username, friend)).thenThrow(new UserNotFoundException());

        mvc.perform(MockMvcRequestBuilders.post("/user/{name}/friend", username).
                content(convertObjectToJsonBytes(friend)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));

        verify(userFacade, times(1)).addToFriendList(anyString(), anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenGetFriendsThenFriendsAreReturned() throws Exception {
        String username = "max";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.setFriends(asList(friend));
        when(userFacade.getFriends(username)).thenReturn(asList(new UserDto(friend.getName(), friend.getUsername(), friend.getDateOfBirth())));

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].name", is(friend.getName()))).
                andExpect(jsonPath("$[0].username", is(friend.getUsername())));

        verify(userFacade, times(1)).getFriends(anyString());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenGetFriendsForNonExistingUserThenResponseStatusIsNotFound() throws Exception {
        String username = "max";

        when(userFacade.getFriends(username)).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));

        verify(userFacade, times(1)).getFriends(anyString());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenUpdateUserThenUserIsUpdated() throws Exception {
        String username = "max";
        String name = "name";
        Date date = new Date(1);
        User user = new User(null, name, date);
        when(userFacade.updateUser(anyObject())).thenReturn(new UserDto(name, username, date));

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.username", is(username))).
                andExpect(jsonPath("$.name", is(name))).
                andExpect(jsonPath("$.dateOfBirth", is(date.toString())));

        verify(userFacade, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(userFacade);
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

        verify(userFacade, times(0)).updateUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenUpdateUserWithNullNameAndNewDateOfBirthThenUserIsUpdated() throws Exception {
        String username = "max";
        Date date = new Date(1);
        String validName = "validName";

        User user = new User(null, null, date);
        when(userFacade.updateUser(anyObject())).thenReturn(new UserDto(validName, username, date));

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.username", is(username))).
                andExpect(jsonPath("$.name", is(validName))).
                andExpect(jsonPath("$.dateOfBirth", is(date.toString())));

        verify(userFacade, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    public void whenUpdateUserWithNullDateOfBirthAndNewNameThenUserIsUpdated() throws Exception {
        String username = "max";
        String name = "name";
        Date date = new Date(1);

        User user = new User(null, name, null);
        when(userFacade.updateUser(anyObject())).thenReturn(new UserDto(name, username, date));

        mvc.perform(MockMvcRequestBuilders.put("/user/{username}", username).
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.username", is(username))).
                andExpect(jsonPath("$.name", is(name))).
                andExpect(jsonPath("$.dateOfBirth", is(date.toString())));

        verify(userFacade, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(userFacade);
    }
}
