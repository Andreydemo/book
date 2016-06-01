package com.epam.cdp.entityHolder;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.User;
import org.springframework.stereotype.Component;

@Component
public class EntityHolderImpl implements EntityHolder {
    private Event event;
    private User user;

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
