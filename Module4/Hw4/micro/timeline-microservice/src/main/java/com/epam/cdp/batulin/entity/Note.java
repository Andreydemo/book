package com.epam.cdp.batulin.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties({"id"})
public class Note implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    private long id;

    @NotEmpty(message = "{NotEmpty.note.username}")
    private String username;

    @Column(name = "note_text")
    @NotEmpty(message = "{NotEmpty.note.noteText}")
    private String noteText;

    public Note() {
    }

    public Note(String noteText) {
        this.noteText = noteText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", noteText='" + noteText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Objects.equals(username, note.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}