package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.handler.QueryHandler;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.epam.cdp.batulin.exception.UserNotFoundException;

import org.hamcrest.core.Is;
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

import static com.epam.cdp.batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.createExceptionResolver;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QueryControllerTest {
    @Mock
    QueryHandler queryHandler;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        queryHandler = mock(QueryHandler.class);
        QueryController queryController = new QueryController();
        queryController.setQueryHandler(queryHandler);
        mvc = MockMvcBuilders.standaloneSetup(queryController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenGetNotesOnUsersTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        List<Note> list = Arrays.asList(note, new Note(""));
        when(queryHandler.getUsersNotes(anyString())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/timeline", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(queryHandler, times(1)).getUsersNotes(anyString());
        verifyNoMoreInteractions(queryHandler);
    }

    @Test
    public void whenGetNotesOnFriendsTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");
        List<Note> list = Arrays.asList(note, new Note(""));

        when(queryHandler.getUsersFriendNotes(anyString(), anyString())).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(queryHandler, times(1)).getUsersFriendNotes(anyString(), anyString());
    }

    @Test
    public void whenGetFriendsThenFriendsAreReturned() throws Exception {
        String username = "max";
        User user = new User(username, "name", new Date(1));
        User friend = new User("friend", "name");
        user.setFriends(asList(friend));
        when(queryHandler.getUserByUsername(username)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].name", Is.is(friend.getName()))).
                andExpect(jsonPath("$[0].username", is(friend.getUsername())));

        verify(queryHandler, times(1)).getUserByUsername(anyString());
        verifyNoMoreInteractions(queryHandler);
    }

    @Test
    public void whenGetFriendsForNonExistingUserThenResponseStatusIsNotFound() throws Exception {
        String username = "max";

        when(queryHandler.getUserByUsername(username)).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", Is.is(HttpStatus.NOT_FOUND.value())));

        verify(queryHandler, times(1)).getUserByUsername(anyString());
        verifyNoMoreInteractions(queryHandler);
    }
}
