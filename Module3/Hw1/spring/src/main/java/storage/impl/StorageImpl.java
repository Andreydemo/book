package storage.impl;

import org.apache.log4j.Logger;
import service.impl.UserServiceImpl;
import storage.Storage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StorageImpl implements Storage {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private Map<String, Object> storage = new HashMap<>();

    @Override
    public <T> T getEntityById(String key) {
        T result = (T) storage.get(key);
        logger.debug("Getting entity by id: " + key + ", " + result);
        return result;
    }

    @Override
    public <T> T put(String key, T element) {
        logger.debug("Putting element to storage with key: " + key + ", element: " + element);
        storage.put(key, element);
        return element;
    }

    @Override
    public boolean delete(String key) {
        logger.debug("Deleting element from storage with key: " + key);
        return storage.remove(key) != null;
    }

    @Override
    public <T> List<T> getElementsByPredicate(String namespace, Predicate<T> predicate, Comparator<T> comparator, int pageSize, int pageNum) {
        logger.debug("Getting elements by predicate with pageSize: " + pageSize + ", pageNum: " + pageNum);
        List<T> list = storage.entrySet().stream().
                filter(e -> e.getKey().startsWith(namespace)).
                map(e -> (T) e.getValue()).
                filter(predicate).
                sorted(comparator).
                skip(pageNum * pageSize - pageSize).
                limit(pageSize).
                collect(Collectors.toList());
        logger.debug("List to return: " + list);
        return list;
    }

    @Override
    public <T> T getFirstByPredicate(String namespace, Predicate<T> predicate) {
        logger.debug("Getting first elements by predicate");
        T result = storage.entrySet().stream().
                filter(e -> e.getKey().startsWith(namespace)).
                map(e -> (T) e.getValue()).
                filter(predicate).
                findFirst().orElse(null);
        logger.debug("Element to return: " + result);
        return result;
    }
}
