package com.epam.cdp.batulin.cqrs.handler;

import com.epam.cdp.batulin.cqrs.event.UserEvent;

public interface EventHandler {
    void handle(UserEvent event);
}
