package postProcessor.parser.impl;

import model.Ticket;
import storage.Storage;

public class TicketsStorageJsonFiller extends AbstractStorageJsonFiller<Ticket> {
    public TicketsStorageJsonFiller(Storage storage, String filePath) {
        super(storage, filePath);
    }

    @Override
    protected void put(Ticket element) {
        logger.debug("Putting element: " + element);
        storage.put("ticket:" + element.getId(), element);
    }
}
