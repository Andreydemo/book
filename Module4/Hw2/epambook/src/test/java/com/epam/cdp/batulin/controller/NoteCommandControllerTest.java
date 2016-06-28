package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.cqrs.handler.CommandHandler;
import com.epam.cdp.batulin.entity.Note;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.epam.cdp.batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.convertObjectToJsonBytes;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.createExceptionResolver;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NoteCommandControllerTest {
    @Mock
    CommandHandler commandHandler;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        commandHandler = mock(CommandHandler.class);
        NoteCommandController noteController = new NoteCommandController();
        noteController.setCommandHandler(commandHandler);
        mvc = MockMvcBuilders.standaloneSetup(noteController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenAddNoteToUsersTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        verify(commandHandler, times(1)).addNote(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenAddNoteToUsersTimelineWithEmptyNoteTextThenResponseStatusIsBadRequest() throws Exception {
        String username = "max";
        Note note = new Note("");

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("Note cannot be added: noteText cannot be empty")));

        verify(commandHandler, times(0)).addNote(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenAddNoteToFriendsTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        verify(commandHandler, times(1)).addNote(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }

    @Test
    public void whenAddNoteToFriendsTimelineWithEmptyNoteTextThenResponseStatusIsBadRequest() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note("");

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("Note cannot be added: noteText cannot be empty")));

        verify(commandHandler, times(0)).addNote(anyObject());
        verifyNoMoreInteractions(commandHandler);
    }
}