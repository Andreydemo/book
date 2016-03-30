package com.epam.cdp.m2.hw2.tasks;

import com.epam.cdp.m2.hw2.tools.Java7Tools;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FrequencyMapTask extends RecursiveTask<Map<String, Integer>> {
    static final int SEQUENTIAL_THRESHOLD = 200;
    private List<String> words;

    public FrequencyMapTask(List<String> words) {
        this.words = words;
    }

    @Override
    protected Map<String, Integer> compute() {
        if (words.size() <= SEQUENTIAL_THRESHOLD) {
            return Java7Tools.getWordFrequencyMap(words);
        } else {
            int middleIndex = words.size() / 2;
            FrequencyMapTask left = new FrequencyMapTask(words.subList(0, middleIndex));
            FrequencyMapTask right = new FrequencyMapTask(words.subList(middleIndex, words.size()));
            left.fork();
            Map<String, Integer> map = mergeMapsFromTasks(left, right);
            return map;
        }
    }

    private Map<String, Integer> mergeMapsFromTasks(FrequencyMapTask left, FrequencyMapTask right) {
        Map<String, Integer> rightResult = right.compute();
        Map<String, Integer> leftResult = left.join();
        for (Map.Entry<String, Integer> entry : rightResult.entrySet()) {
            if (leftResult.containsKey(entry.getKey())) {
                leftResult.put(entry.getKey(), entry.getValue() + leftResult.get(entry.getKey()));
            } else {
                leftResult.put(entry.getKey(), entry.getValue());
            }
        }
        return leftResult;
    }
}
