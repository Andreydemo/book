package batulin.service;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.repository.NoteRepository;
import com.epam.cdp.batulin.service.impl.NoteServiceImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class NoteServiceTest {
    private NoteServiceImpl noteService;

    @Mock
    NoteRepository noteRepository;

    @Mock
    JmsTemplate jmsTemplate;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        noteRepository = mock(NoteRepository.class);
        jmsTemplate = mock(JmsTemplate.class);
        noteService = new NoteServiceImpl();
        noteService.setNoteRepository(noteRepository);
        noteService.setJmsTemplate(jmsTemplate);
    }

    @Test
    public void whenAddNoteTheNoteIsAdded() {
        String username = "username";
        Note note = new Note("noteText");
        note.setUsername(username);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note addedNote = noteService.addNote(note);
        assertEquals(note.getNoteText(), addedNote.getNoteText());
        assertEquals(note.getUsername(), username);
    }

    @Test
    public void whenGetNotesTheListOfNotesIsReturned() {
        String username = "username";
        Note note = new Note("noteText");

        when(noteRepository.findByUsername(anyString())).thenReturn(asList(note));

        List<Note> notes = noteService.getUsersNotes(username);
        assertEquals(notes.get(0), note);
    }
}