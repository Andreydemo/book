package postProcessor.filler.impl;

import model.Ticket;

public class TicketsStorageJsonFiller extends AbstractStorageJsonFiller<Ticket> {
    public TicketsStorageJsonFiller(String filePath) {
        super(filePath);
    }

    @Override
    protected void put(Ticket element) {
        logger.debug("Putting element: " + element);
        storage.put("ticket:" + element.getId(), element);
    }
}
