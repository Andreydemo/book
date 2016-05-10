package postProcessor.filler.impl;

import model.User;

public class UsersStorageJsonFiller extends AbstractStorageJsonFiller<User> {

    public UsersStorageJsonFiller(String filePath) {
        super(filePath);
    }

    @Override
    protected void put(User element) {
        logger.debug("Putting element: " + element);
        storage.put("user:" + element.getId(), element);
    }
}
