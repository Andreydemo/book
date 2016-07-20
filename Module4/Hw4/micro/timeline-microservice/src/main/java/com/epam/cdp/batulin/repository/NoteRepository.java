package com.epam.cdp.batulin.repository;

import com.epam.cdp.batulin.entity.Note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUsername(String username);
}
