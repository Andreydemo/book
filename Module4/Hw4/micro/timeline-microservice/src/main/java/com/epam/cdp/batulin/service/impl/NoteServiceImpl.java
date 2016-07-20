package com.epam.cdp.batulin.service.impl;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.repository.NoteRepository;
import com.epam.cdp.batulin.service.NoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    @Override
    public Note addNote(final Note note) {
        Note createdNote = noteRepository.save(note);
        sendToArchive(note);
        return createdNote;
    }

    @Override
    public List<Note> getUsersNotes(String username) {
        return noteRepository.findByUsername(username);
    }

    private void sendToArchive(Note note) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", note.getId());
        map.put("noteText", note.getNoteText());
        map.put("username", note.getUsername());
        jmsTemplate.convertAndSend("noteQueue", map);
    }

    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}