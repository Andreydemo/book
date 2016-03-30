package com.epam.cdp.m2.hw2.timeMeasure;

import com.epam.cdp.m2.hw2.aggregator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeTest {
    public static void main(String[] args) {
        List<Integer> integerList = getRandomIntegerList(5_000_000);
        List<String> stringList = getRandomStringList(5_000_000);
        Aggregator aggregator = new Java7Aggregator();
        System.out.println();
        System.out.println("Java 7 aggregator:");
        Timeit.testSum(aggregator, integerList);
        Timeit.testFrequency(aggregator, stringList, stringList.size());
        Timeit.testDuplicates(aggregator, stringList, stringList.size());

        aggregator = new Java7ParallelAggregator();
        System.out.println();
        System.out.println("Java 7 parallel aggregator:");
        Timeit.testSum(aggregator, integerList);
        Timeit.testFrequency(aggregator, stringList, stringList.size());

        aggregator = new Java8Aggregator();
        System.out.println();
        System.out.println("Java 8 aggregator:");
        Timeit.testSum(aggregator, integerList);
        Timeit.testFrequency(aggregator, stringList, stringList.size());
        Timeit.testDuplicates(aggregator, stringList, stringList.size());

        aggregator = new Java8ParallelAggregator();
        System.out.println();
        System.out.println("Java 8 parallel aggregator:");
        Timeit.testSum(aggregator, integerList);
        Timeit.testFrequency(aggregator, stringList, stringList.size());
        Timeit.testDuplicates(aggregator, stringList, stringList.size());
    }

    private static List<Integer> getRandomIntegerList(int size) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++)
            result.add((int) (Math.random() * 10));
        return result;
    }

    private static List<String> getRandomStringList(int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++)
            result.add(UUID.randomUUID().toString().replaceAll("-", ""));
        return result;
    }
}
