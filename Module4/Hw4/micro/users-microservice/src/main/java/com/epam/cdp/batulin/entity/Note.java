package com.epam.cdp.batulin.entity;


import java.util.Objects;

public class Note {
    private long id;
    private String username;
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