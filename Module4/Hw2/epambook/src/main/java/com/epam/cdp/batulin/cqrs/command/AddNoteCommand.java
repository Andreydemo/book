package com.epam.cdp.batulin.cqrs.command;

import org.hibernate.validator.constraints.NotEmpty;

public class AddNoteCommand implements UserCommand {
    private String authorUsername;
    private String assigneeUsername;

    @NotEmpty(message = "{NotEmpty.note.noteText}")
    private String noteText;

    @Override
    public String getUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public String toString() {
        return "AddNoteCommand{" +
                ", noteText='" + noteText + '\'' +
                '}';
    }
}
