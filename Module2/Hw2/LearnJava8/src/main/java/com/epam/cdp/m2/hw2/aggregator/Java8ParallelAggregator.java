package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Java8ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.parallelStream().reduce(0, (a, b) -> a + b);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.parallelStream().collect(groupingBy(Function.identity(), counting())).
                entrySet().parallelStream().map(e -> new Pair<>(e.getKey(), e.getValue())).
                sorted((a, b) -> a.getValue().equals(b.getValue()) ?
                        a.getKey().compareTo(b.getKey()) :
                        b.getValue().compareTo(a.getValue())).
                limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        final Set<String> set1 = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
        return words.parallelStream().filter(string -> !set1.add(string)).
                map(String::toUpperCase).
                sorted((a, b) -> a.length() == b.length() ? a.compareTo(b) : a.length() - b.length()).
                limit(limit).collect(Collectors.toList());
    }
}
