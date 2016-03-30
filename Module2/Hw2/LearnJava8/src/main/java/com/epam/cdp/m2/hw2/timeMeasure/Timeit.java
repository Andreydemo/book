package com.epam.cdp.m2.hw2.timeMeasure;

import com.epam.cdp.m2.hw2.aggregator.Aggregator;

import java.util.List;

public final class Timeit {

    private Timeit() {
    }

    public static void testSum(Aggregator aggregator, List<Integer> list) {
        long now = System.currentTimeMillis();
        aggregator.sum(list);
        System.out.println("Sum time: " + (System.currentTimeMillis() - now) + " millis");
    }

    public static void testFrequency(Aggregator aggregator, List<String> list, int limit) {
        long now = System.currentTimeMillis();
        aggregator.getMostFrequentWords(list, limit);
        System.out.println("Frequency time: " + (System.currentTimeMillis() - now) + " millis");
    }

    public static void testDuplicates(Aggregator aggregator, List<String> list, int limit) {
        long now = System.currentTimeMillis();
        aggregator.getDuplicates(list, limit);
        System.out.println("Duplicates time: " + (System.currentTimeMillis() - now) + " millis");
    }
}