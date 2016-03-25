package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Java7ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return ForkJoinPool.commonPool().invoke(new Sum(numbers));
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}

class Sum extends RecursiveTask<Integer> {
    static final int SEQUENTIAL_THRESHOLD = 2;

    List<Integer> numbers;

    Sum(List<Integer> numbers) {
        this.numbers = numbers;
    }

    protected Integer compute() {
        if(numbers.size() <= SEQUENTIAL_THRESHOLD) {
            int result = 0;
            for(int i = 0; i < numbers.size(); ++i)
                result += numbers.get(i);
            return result;
        } else {
            int middleIndex = numbers.size() / 2;
            Sum left  = new Sum(numbers.subList(0, middleIndex));
            Sum right = new Sum(numbers.subList(middleIndex, numbers.size()));
            left.fork();
            int rightResult = right.compute();
            int leftResult  = left.join();
            return leftResult + rightResult;
        }
    }
}
