package com.epam.cdp.m2.hw2.aggregator;

import com.epam.cdp.m2.hw2.tasks.FrequencyMapTask;
import com.epam.cdp.m2.hw2.tasks.SumTask;
import com.epam.cdp.m2.hw2.tools.Java7Tools;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class Java7ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return ForkJoinPool.commonPool().invoke(new SumTask(numbers));
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        Map<String, Integer> map = ForkJoinPool.commonPool().invoke(new FrequencyMapTask(words));
        List<Pair<String, Long>> result = Java7Tools.getPairList(map);
        Java7Tools.sortListOfPairs(result);
        return limit < result.size() ? result.subList(0, (int) limit) : result;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}
