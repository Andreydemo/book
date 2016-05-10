package postProcessor.filler.impl;

import model.Event;

public class EventsStorageJsonFiller extends AbstractStorageJsonFiller<Event> {

    public EventsStorageJsonFiller(String filePath) {
        super(filePath);
    }

    @Override
    protected void put(Event element) {
        logger.debug("Putting element: " + element);
        storage.put("event:" + element.getId(), element);
    }
}
