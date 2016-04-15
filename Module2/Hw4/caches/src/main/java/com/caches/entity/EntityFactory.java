package com.caches.entity;

public class EntityFactory {

    private static int id = 0;

    public Entity createEntity() {
        return new Entity(nextId());
    }

    private String nextId() {
        return String.valueOf(id++);
    }

}
