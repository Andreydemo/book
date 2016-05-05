package postProcessor.filler.impl;

import model.User;
import storage.Storage;

public class UsersStorageJsonFiller extends AbstractStorageJsonFiller<User> {

    public UsersStorageJsonFiller(Storage storage, String filePath) {
        super(storage, filePath);
    }

    @Override
    protected void put(User element) {
        logger.debug("Putting element: " + element);
        storage.put("user:" + element.getId(), element);
    }
}
