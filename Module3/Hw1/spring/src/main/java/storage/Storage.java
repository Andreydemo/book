package storage;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface Storage {

    <T> T getEntityById(String key);

    <T> T put(String key, T element);

    boolean delete(String key);

    <T> List<T> getElementsByPredicate(String namespace, Predicate<T> predicate, Comparator<T> comparator, int pageSize, int pageNum);

    <T> T getFirstByPredicate(String namespace, Predicate<T> predicate);
}
