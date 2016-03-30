package com.epam.cdp.m2.hw2.tasks;

import com.epam.cdp.m2.hw2.tools.Java7Tools;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Integer> {
    static final int SEQUENTIAL_THRESHOLD = 2000;
    private List<Integer> numbers;

    public SumTask(List<Integer> numbers) {
        this.numbers = numbers;
    }

    protected Integer compute() {
        if (numbers.size() <= SEQUENTIAL_THRESHOLD) {
            return Java7Tools.sum(numbers);
        } else {
            int middleIndex = numbers.size() / 2;
            SumTask left = new SumTask(numbers.subList(0, middleIndex));
            SumTask right = new SumTask(numbers.subList(middleIndex, numbers.size()));
            left.fork();
            int rightResult = right.compute();
            int leftResult = left.join();
            return leftResult + rightResult;
        }
    }
}