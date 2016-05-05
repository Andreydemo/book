package storage;

import org.junit.Before;
import org.junit.Test;
import storage.impl.StorageImpl;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StorageTest {
    private Storage storage;

    @Before
    public void setUp() {
        storage = new StorageImpl();
    }

    @Test
    public void whenGetEntityByIdThenEntityIsReturned() {
        Integer expected = 2;
        storage.put("test", expected);
        Integer result = storage.getEntityById("test");
        assertEquals(expected, result);
    }

    @Test
    public void whenPutElementToStorageThenOneIsPut() {
        Integer expected = 2;
        storage.put("test", expected);
        Integer result = storage.getEntityById("test");
        assertEquals(expected, result);
    }

    @Test
    public void whenDeleteThenEntityIsDeleted() {
        String test = "test";
        storage = new StorageImpl();
        storage.put(test, 2);
        storage.delete(test);
        assertNull(storage.getEntityById(test));
    }

    @Test
    public void whenGetFirstByPredicateThenFirstElementIsResurned() {
        Integer expected = 2;
        storage.put("test", expected);
        storage.put("not-test", 3);
        Predicate<Integer> predicate = o -> true;
        Integer result = storage.getFirstByPredicate("test", predicate);
        assertEquals(expected, result);
    }

    @Test
    public void whenGetElementsByPredicateWithTenPageSizeThenElementsAreReturned() {
        storage.put("test:1", 2);
        storage.put("test:2", 3);
        storage.put("test:3", 4);
        storage.put("not-test", 8);
        Predicate<Integer> predicate = o -> true;
        Comparator<Integer> comparator = Integer::compareTo;
        List<Integer> result = storage.getElementsByPredicate("test", predicate, comparator, 10, 1);
        assertEquals(Arrays.asList(2, 3, 4), result);
    }

    @Test
    public void whenGetElementsByPredicateWithTwoPageSizeThenOnlyFirstTwoElementsAreReturned() {
        storage.put("test:1", 2);
        storage.put("test:2", 3);
        storage.put("test:3", 4);
        storage.put("not-test", 8);
        Predicate<Integer> predicate = o -> true;
        Comparator<Integer> comparator = Integer::compareTo;
        List<Integer> result = storage.getElementsByPredicate("test", predicate, comparator, 2, 1);
        assertEquals(Arrays.asList(2, 3), result);
    }
}
