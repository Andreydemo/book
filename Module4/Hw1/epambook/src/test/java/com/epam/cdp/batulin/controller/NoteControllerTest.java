package com.epam.cdp.batulin.controller;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.service.NoteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.epam.cdp.batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.convertObjectToJsonBytes;
import static com.epam.cdp.batulin.controller.ControllerTestUtils.createExceptionResolver;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NoteControllerTest {
    @Mock
    NoteService noteService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        noteService = mock(NoteService.class);
        NoteController noteController = new NoteController();
        noteController.setNoteService(noteService);
        mvc = MockMvcBuilders.standaloneSetup(noteController).
                setHandlerExceptionResolvers(createExceptionResolver()).
                build();
    }

    @Test
    public void whenAddNoteToUsersTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        when(noteService.addNote(anyObject(), anyString())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.noteText", is(note.getNoteText())));

        verify(noteService, times(1)).addNote(anyObject(), anyString());
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenAddNoteToUsersTimelineWithEmptyNoteTextThenResponseStatusIsBadRequest() throws Exception {
        String username = "max";
        Note note = new Note("");
        when(noteService.addNote(anyObject(), anyString())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("Note cannot be added: noteText cannot be empty")));

        verify(noteService, times(0)).addNote(anyObject(), anyString());
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenGetNotesOnUsersTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        List<Note> list = Arrays.asList(note, new Note(""));
        when(noteService.getUsersNotes(anyString())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/timeline", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(noteService, times(1)).getUsersNotes(anyString());
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenGetNotesOnFriendsTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");
        List<Note> list = Arrays.asList(note, new Note(""));

        when(noteService.getUsersFriendNotes(anyString(), anyString())).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(noteService, times(1)).getUsersFriendNotes(anyString(), anyString());
    }

    @Test
    public void whenAddNoteToFriendsTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note();
        note.setNoteText("Some text");
        when(noteService.addNoteToFriendsTimeline(anyString(), anyString(), anyObject())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.noteText", is(note.getNoteText())));

        verify(noteService, times(1)).addNoteToFriendsTimeline(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenAddNoteToFriendsTimelineWithEmptyNoteTextThenResponseStatusIsBadRequest() throws Exception {
        String username = "max";
        String friendUsername = "andrew";
        Note note = new Note("");
        when(noteService.addNoteToFriendsTimeline(anyString(), anyString(), anyObject())).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/user/{username}/friend/{friend}/timeline", username, friendUsername).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("Note cannot be added: noteText cannot be empty")));

        verify(noteService, times(0)).addNoteToFriendsTimeline(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(noteService);
    }
}
