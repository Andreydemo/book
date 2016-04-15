package com.caches.cache;

import com.caches.dao.CrudDao;

public class CacheFactory {

    public static final int WRITE_BEHIND_INTERVAL_MILLIS = 6000;

    public <KEY, VAL> Cache<KEY, VAL> createEntityCache(int maxSize, CrudDao<KEY, VAL> systemOfRecord) {
        return new CacheImpl.CacheBuilder().
                systemOfRecord(systemOfRecord).
                cacheSize(maxSize).
                writeBehindIntervalMillis(WRITE_BEHIND_INTERVAL_MILLIS).
                build();
    }

}
