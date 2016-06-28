package com.epam.cdp.batulin.cqrs.aggregateRoot;

public class Note {
    private String author;
    private String noteText;

    public Note(String author, String noteText) {
        this.author = author;
        this.noteText = noteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
