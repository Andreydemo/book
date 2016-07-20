package com.epam.cdp.batulin.service.impl;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.exception.NoteNotAddedException;
import com.epam.cdp.batulin.service.NoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestNoteService implements NoteService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${timelineService.baseUrl}")
    private String baseUrl;

    @Override
    public Note addNote(Note note) {
        ResponseEntity<Note> noteResponseEntity = restTemplate.postForEntity(baseUrl + "/timeline", note, Note.class);
        if (noteResponseEntity.getStatusCode() != HttpStatus.CREATED)
            throw new NoteNotAddedException();
        return noteResponseEntity.getBody();
    }

    @Override
    public List<Note> getUsersNotes(String username) {
        String url = baseUrl + "/timeline/{username}";
        System.out.println(url);
        return restTemplate.getForObject(baseUrl + "/timeline/{username}", List.class, username);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
