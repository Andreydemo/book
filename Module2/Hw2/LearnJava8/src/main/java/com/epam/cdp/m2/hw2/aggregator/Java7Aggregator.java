package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        int result = 0;
        for (Integer number : numbers)
            result += number;
        return result;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        Map<String, Integer> map = getWordFrequencyMap(words);
        List<Pair<String, Long>> pairs = getUnsortedPairs(map);
        sortListOfPairs(pairs);
        if (limit < pairs.size())
            return pairs.subList(0, (int) limit);
        return pairs;
    }

    private Map<String, Integer> getWordFrequencyMap(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            if (map.containsKey(word))
                map.put(word, map.get(word)+1);
            else {
                map.put(word, 1);
            }
        }
        return map;
    }

    private List<Pair<String, Long>> getUnsortedPairs(Map<String, Integer> map) {
        List<Pair<String, Long>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: map.entrySet()) {
            result.add(new Pair<>(entry.getKey(), entry.getValue().longValue()));
        }
        return result;
    }

    private void sortListOfPairs(List<Pair<String, Long>> result) {
        Collections.sort(result, new Comparator<Pair<String, Long>>() {
            @Override
            public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}
