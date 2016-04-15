package com.caches.cache;

import com.caches.dao.CrudDao;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class CacheImpl<KEY, VAL> implements Cache<KEY, VAL> {
    private CrudDao<KEY, VAL> systemOfRecord;
    //package scope for testing purposes
    Map<KEY, CacheEntry> cache;
    private BlockingQueue<VAL> writeBehindQueue;
    private final int cacheSize;
    private ReentrantLock lock = new ReentrantLock();

    private CacheImpl(CrudDao<KEY, VAL> systemOfRecord, int cacheSize, int writeBehindIntervalMillis) {
        this.systemOfRecord = systemOfRecord;
        writeBehindQueue = new LinkedBlockingQueue<>();
        cache = new ConcurrentHashMap<>();
        this.cacheSize = cacheSize;
        runWriteBehindThread(writeBehindIntervalMillis);
    }

    public VAL get(KEY key) {
        VAL value;
        if (cache.containsKey(key)) {
            lock.lock();
            CacheEntry entry = cache.get(key);
            entry.frequency++;
            value = entry.data;
            lock.unlock();
        } else {
            value = systemOfRecord.read(key);
            putNewEntryToCache(key, value);
        }
        return value;
    }

    public void put(KEY key, VAL value) {
        if (cache.containsKey(key)) {
            CacheEntry cacheEntry = cache.get(key);
            cacheEntry.data = value;
        } else {
            putNewEntryToCache(key, value);
        }
        putToWriteBehindQueue(value);
    }

    private void putNewEntryToCache(KEY key, VAL value) {
        lock.lock();
        if (isFull()) {
            KEY entryKeyToBeRemoved = getLfuKey();
            cache.remove(entryKeyToBeRemoved);
        }
        CacheEntry cacheEntry = new CacheEntry(value, 0);
        cache.put(key, cacheEntry);
        lock.unlock();
    }

    private void putToWriteBehindQueue(VAL value) {
        try {
            writeBehindQueue.put(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isFull() {
        return cache.size() == cacheSize;
    }

    private void runWriteBehindThread(final int writeBehindIntervalMillis) {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (true) {
                    try {
                        wait(writeBehindIntervalMillis);
                        while (!writeBehindQueue.isEmpty()) {
                            CacheImpl.this.systemOfRecord.update(writeBehindQueue.take());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private KEY getLfuKey() {
        KEY key = null;
        int minFreq = Integer.MAX_VALUE;
        for (Map.Entry<KEY, CacheEntry> entry : cache.entrySet()) {
            if (minFreq > entry.getValue().frequency) {
                key = entry.getKey();
                minFreq = entry.getValue().frequency;
            }
        }
        return key;
    }

    class CacheEntry {
        private VAL data;
        private int frequency;

        CacheEntry(VAL data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        public VAL getData() {
            return data;
        }

        public int getFrequency() {
            return frequency;
        }
    }

    static class CacheBuilder<KEY, VAL> {
        private CrudDao<KEY, VAL> systemOfRecord;
        private int cacheSize;
        private int writeBehindIntervalMillis;

        CacheBuilder systemOfRecord(CrudDao<KEY, VAL> systemOfRecord) {
            this.systemOfRecord = systemOfRecord;
            return this;
        }

        CacheBuilder cacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        CacheBuilder writeBehindIntervalMillis(int writeBehindIntervalMillis) {
            this.writeBehindIntervalMillis = writeBehindIntervalMillis;
            return this;
        }

        CacheImpl build() {
            return new CacheImpl(systemOfRecord, cacheSize, writeBehindIntervalMillis);
        }
    }
}