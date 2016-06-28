package com.epam.cdp.batulin.cqrs.event;

public class NoteAddedEvent extends UserEvent {
    private String assigneeUsername;
    private String noteText;

    public NoteAddedEvent(String username, String assigneeUsername, String noteText) {
        super(username);
        this.assigneeUsername = assigneeUsername;
        this.noteText = noteText;
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }
}