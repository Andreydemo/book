package com.caches.cache;


/**
 * Eviction strategy - LFU.
 * Read-through.
 * Write-behind.
 */
public interface Cache<KEY, VAL> {

    /**
     * Returns cached value or goes to the system of record and then caches and returns the result.
     */
    VAL get(KEY key);

    /**
     * Should work as upsert. If there is already such record in the system of record, update it.
     * If there is no such record in the system of record, insert it. Should cache the updated/inserted record.
     */
    void put(KEY key, VAL value);


}
