package postProcessor.parser.impl;

import model.Event;
import storage.Storage;

public class EventsStorageJsonFiller extends AbstractStorageJsonFiller<Event> {

    public EventsStorageJsonFiller(Storage storage, String filePath) {
        super(storage, filePath);
    }

    @Override
    protected void put(Event element) {
        logger.debug("Putting element: " + element);
        storage.put("event:" + element.getId(), element);
    }
}
