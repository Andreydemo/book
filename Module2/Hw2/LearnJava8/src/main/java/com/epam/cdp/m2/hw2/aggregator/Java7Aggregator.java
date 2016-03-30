package com.epam.cdp.m2.hw2.aggregator;

import com.epam.cdp.m2.hw2.tools.Java7Tools;
import javafx.util.Pair;

import java.util.*;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return Java7Tools.sum(numbers);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        Map<String, Integer> map = Java7Tools.getWordFrequencyMap(words);
        List<Pair<String, Long>> pairs = Java7Tools.getPairList(map);
        return limit < pairs.size() ? pairs.subList(0, (int) limit) : pairs;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        List<String> result = Java7Tools.getListWithDuplicates(words);
        return limit < result.size() ? result.subList(0, (int) limit) : result;
    }
}
