package com.epam.cdp.batulin.repository;

import com.epam.cdp.batulin.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
