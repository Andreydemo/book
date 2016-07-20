package batulin.controller;

import com.epam.cdp.batulin.controller.TimelineController;
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

import static batulin.controller.ControllerTestUtils.APPLICATION_JSON_UTF8;
import static batulin.controller.ControllerTestUtils.convertObjectToJsonBytes;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
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

public class TimelineControllerTest {
    @Mock
    NoteService noteService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        noteService = mock(NoteService.class);
        TimelineController TimelineController = new TimelineController();
        TimelineController.setNoteService(noteService);
        mvc = MockMvcBuilders.standaloneSetup(TimelineController).
                build();
    }

    @Test
    public void whenAddNoteToUsersTimelineThenNoteIsAdded() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        note.setUsername(username);
        when(noteService.addNote(any(Note.class))).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/timeline", username).
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.noteText", is(note.getNoteText())));

        verify(noteService, times(1)).addNote(any(Note.class));
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenAddNoteToUsersTimelineWithEmptyNoteTextThenResponseStatusIsBadRequest() throws Exception {
        String username = "max";
        Note note = new Note("");
        note.setUsername(username);
        when(noteService.addNote(any(Note.class))).thenReturn(note);

        mvc.perform(MockMvcRequestBuilders.post("/timeline").
                content(convertObjectToJsonBytes(note)).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value()))).
                andExpect(jsonPath("$.message", is("Note cannot be added: noteText cannot be empty")));

        verify(noteService, times(0)).addNote(any(Note.class));
        verifyNoMoreInteractions(noteService);
    }

    @Test
    public void whenGetNotesOnUsersTimelineThenNotesAreReturned() throws Exception {
        String username = "max";
        Note note = new Note();
        note.setNoteText("Some text");
        note.setUsername(username);
        List<Note> list = Arrays.asList(note, new Note(""));
        when(noteService.getUsersNotes(anyString())).thenReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/timeline/{username}", username).
                contentType(APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_UTF8)).
                andExpect(jsonPath("$[0].noteText", is(note.getNoteText()))).
                andExpect(jsonPath("$[1].noteText", is("")));

        verify(noteService, times(1)).getUsersNotes(anyString());
        verifyNoMoreInteractions(noteService);
    }
}
