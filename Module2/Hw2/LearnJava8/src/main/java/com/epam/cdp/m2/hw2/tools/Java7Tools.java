package com.epam.cdp.m2.hw2.tools;

import javafx.util.Pair;

import java.util.*;

public class Java7Tools {
    private Java7Tools() {
    }

    public static int sum(List<Integer> numbers) {
        int result = 0;
        for (Integer number : numbers)
            result += number;
        return result;
    }

    public static Map<String, Integer> getWordFrequencyMap(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            if (map.containsKey(word))
                map.put(word, map.get(word) + 1);
            else {
                map.put(word, 1);
            }
        }
        return map;
    }

    public static List<Pair<String, Long>> getPairList(Map<String, Integer> map) {
        List<Pair<String, Long>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.add(new Pair<>(entry.getKey(), entry.getValue().longValue()));
        }
        sortListOfPairs(result);
        return result;
    }

    public static void sortListOfPairs(List<Pair<String, Long>> result) {
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

    public static List<String> getListWithDuplicates(List<String> words) {
        List<String> result = new ArrayList<>();
        Set<String> strings = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String word : words) {
            if (!strings.add(word)) {
                result.add(word.toUpperCase());
            }
        }
        sortListOfDuplicates(result);
        return result;
    }

    public static void sortListOfDuplicates(List<String> result) {
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() == o2.length() ? o1.compareTo(o2) : o1.length() - o2.length();
            }
        });
    }
}
