package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Java8Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.stream().reduce(0, (a, b) -> a + b);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.stream().collect(groupingBy(Function.identity(), counting())).
                entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).
                sorted((a, b) -> a.getValue().equals(b.getValue()) ?
                        a.getKey().compareTo(b.getKey()) :
                        b.getValue().compareTo(a.getValue())).
                limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        return words.stream().map(String::toUpperCase).
                collect(groupingBy(Function.identity(), counting())).
                entrySet().
                stream().filter(e -> e.getValue() > 1).
                map(Map.Entry::getKey).
                sorted((a, b) -> a.length() == b.length() ? a.compareTo(b) : a.length() - b.length()).
                limit(limit).
                collect(Collectors.toList());
    }
}