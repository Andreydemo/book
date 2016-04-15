package com.caches.cache;

import com.caches.dao.InMemoryEntityDao;
import com.caches.entity.Entity;
import com.caches.entity.EntityFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {

    private static final int CACHE_MAX_SIZE = 2;

    private CacheFactory cacheFactory = new CacheFactory();
    private EntityFactory entityFactory = new EntityFactory();

    private Cache<String, Entity> cache;
    private InMemoryEntityDao dao;

    @Before
    public void init() {
        dao = new InMemoryEntityDao();
        cache = cacheFactory.createEntityCache(CACHE_MAX_SIZE, dao);
    }

    @Test
    public void testReadThrough() {
        Entity entity = entityFactory.createEntity();
        dao.create(entity);
        // at this point cache is empty and entity is not cached
        assertNotCached(entity); // reading through the cache
        // now entity should be cached
        assertCached(entity);
    }

    @Test
    public void testWriteBehind() {
        Entity entity = entityFactory.createEntity();
        // insert the entity through the cache
        long beforeCachePut = System.currentTimeMillis();
        cache.put(entity.getId(), entity);
        long afterCachePut = System.currentTimeMillis();
        // entity should be in the cache at once
        assertCached(entity);
        // after the delay entity should be present in the SOR too
        waitTwiceWriteDelay();
        Entity fromSor = dao.read(entity.getId());

        Assert.assertNotNull("cache should have put the record into the SOR", fromSor);
        // assert we did not have to wait for the response from the SOR
        Assert.assertTrue(afterCachePut - beforeCachePut < InMemoryEntityDao.WRITE_DELAY);
    }

    @Test
    public void testLfuEviction() {
        Entity firstEntity = entityFactory.createEntity();
        Entity secondEntity = entityFactory.createEntity();
        Entity thirdEntity = entityFactory.createEntity();

        // put two entities
        cache.put(firstEntity.getId(), firstEntity);
        cache.put(secondEntity.getId(), secondEntity);
        // use cached values different number of times
        cache.get(firstEntity.getId());
        cache.get(secondEntity.getId());
        cache.get(secondEntity.getId());
        // trigger eviction procedure
        cache.put(thirdEntity.getId(), thirdEntity);
        // expect the cache to evict the least frequently used
        assertCached(secondEntity);
        assertCached(thirdEntity);
        assertNotCached(firstEntity);
    }

    @Test
    public void testConcurrentPut() {
        cache = cacheFactory.createEntityCache(6, dao);

        final Entity firstEntity = entityFactory.createEntity();
        final Entity secondEntity = entityFactory.createEntity();
        final Entity thirdEntity = entityFactory.createEntity();
        final Entity fourthEntity = entityFactory.createEntity();
        final Entity fifthEntity = entityFactory.createEntity();
        final Entity sixthEntity = entityFactory.createEntity();

        //run three threads to put entries concurrently
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.put(firstEntity.getId(), firstEntity);
                cache.put(secondEntity.getId(), secondEntity);
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.put(thirdEntity.getId(), thirdEntity);
                cache.put(fourthEntity.getId(), fourthEntity);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.put(sixthEntity.getId(), fifthEntity);
                cache.put(fifthEntity.getId(), fifthEntity);
            }
        });

        thread.start();
        thread1.start();
        thread2.start();

        try {
            thread.join();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check if all items are in cache
        assertCached(firstEntity);
        assertCached(secondEntity);
        assertCached(thirdEntity);
        assertCached(fourthEntity);
        assertCached(fifthEntity);
    }

    @Test
    public void testMultithreadingGet() {
        final CacheImpl<String, Entity> cache = new CacheImpl.CacheBuilder<>().
                cacheSize(5).
                systemOfRecord(dao).
                writeBehindIntervalMillis(6000).
                build();

        final Entity firstEntity = entityFactory.createEntity();
        final Entity secondEntity = entityFactory.createEntity();
        final Entity thirdEntity = entityFactory.createEntity();
        final Entity fourthEntity = entityFactory.createEntity();
        final Entity fifthEntity = entityFactory.createEntity();

        //put all entries to cache
        cache.put(firstEntity.getId(), firstEntity);
        cache.put(secondEntity.getId(), secondEntity);
        cache.put(thirdEntity.getId(), thirdEntity);
        cache.put(fourthEntity.getId(), fourthEntity);
        cache.put(fifthEntity.getId(), fifthEntity);

        //run three threads to get elements from cache concurrently
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.get(firstEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(secondEntity.getId());
                cache.get(secondEntity.getId());
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.get(secondEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(secondEntity.getId());
                cache.get(thirdEntity.getId());
                cache.get(thirdEntity.getId());
                cache.get(fifthEntity.getId());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                cache.get(firstEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(firstEntity.getId());
                cache.get(fourthEntity.getId());
                cache.get(fourthEntity.getId());
                cache.get(fifthEntity.getId());
                cache.get(thirdEntity.getId());
            }
        });

        thread.start();
        thread1.start();
        thread2.start();

        try {
            thread.join();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check if all the "gets" were considered
        assertEquals(8, cache.cache.get(firstEntity.getId()).getFrequency());
        assertEquals(4, cache.cache.get(secondEntity.getId()).getFrequency());
        assertEquals(3, cache.cache.get(thirdEntity.getId()).getFrequency());
        assertEquals(2, cache.cache.get(fourthEntity.getId()).getFrequency());
        assertEquals(2, cache.cache.get(fifthEntity.getId()).getFrequency());
    }

    private void waitTwiceWriteDelay() {
        try {
            Thread.sleep(InMemoryEntityDao.WRITE_DELAY * 2);
        } catch (InterruptedException e) {
            // ignored
        }
    }

    private void assertCached(Entity entity) {
        long before = System.currentTimeMillis();
        Entity cached = cache.get(entity.getId());
        long after = System.currentTimeMillis();
        Assert.assertNotNull("entity should have been in the cache", cached);
        // if the delay is less then dao delay, we definitely did not call dao method and used cache only
        Assert.assertTrue(after - before < InMemoryEntityDao.READ_DELAY);
    }

    private void assertNotCached(Entity entity) {
        long before = System.currentTimeMillis();
        cache.get(entity.getId());
        long after = System.currentTimeMillis();
        // if the delay is more or equal to the dao delay, then we called dao method
        Assert.assertTrue(after - before >= InMemoryEntityDao.READ_DELAY);
    }

}
