package com.caches.dao;

import com.caches.utils.Assert;
import com.caches.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class InMemoryEntityDao implements CrudDao<String, Entity> {

    public static final long READ_DELAY = 4000;
    public static final long WRITE_DELAY = 5000;

    private final Map<String/*id*/, Entity> storage = new HashMap<String, Entity>();

    public Entity read(String id) {
        Assert.hasText(id, "id must not be null or empty");
        readDelay();
        return storage.get(id);
    }

    public void update(Entity entity) {
        create(entity);
    }

    public void create(Entity entity) {
        Assert.notNull(entity, "entity must not be null");
        Assert.hasText(entity.getId(), "entity must have not empty id");
        writeDelay();
        storage.put(entity.getId(), entity);
    }

    public void delete(String id) {
        Assert.hasText(id, "id must not be null or empty");
        writeDelay();
        storage.remove(id);
    }

    private void writeDelay() {
        delay(WRITE_DELAY);
    }

    private void readDelay() {
        delay(READ_DELAY);
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignored
        }
    }


}
