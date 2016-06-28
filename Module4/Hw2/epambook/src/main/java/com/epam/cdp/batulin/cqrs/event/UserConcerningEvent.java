package com.epam.cdp.batulin.cqrs.event;

import java.util.Date;

public class UserConcerningEvent extends UserEvent {
    private String name;
    private Date dateOfBirth;

    public UserConcerningEvent(String username, String name, Date dateOfBirth) {
        super(username);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
