package com.epam.cdp.entityHolder;

import com.epam.cdp.model.Event;
import com.epam.cdp.model.User;

public interface EntityHolder {
    Event getEvent();

    void setEvent(Event event);

    User getUser();

    void setUser(User user);
}
