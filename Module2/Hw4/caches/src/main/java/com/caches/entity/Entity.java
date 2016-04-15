package com.caches.entity;

import com.caches.utils.Assert;

public class Entity {

    private String id;

    public Entity(String id) {
        Assert.hasText(id, "id must not be null or empty");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        return id.equals(entity.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
