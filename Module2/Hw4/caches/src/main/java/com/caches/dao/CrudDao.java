package com.caches.dao;

public interface CrudDao<ID, E> {

    E read(ID id);

    void update(E entity);

    void create(E entity);

    void delete(ID id);
}
