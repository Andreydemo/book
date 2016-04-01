package com.epam.m2.hw3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuilderTest {
    public static final int EXPECTED_AGE = 20;
    public static final String ANDREW = "Andrew";
    public static final double EXPECTED_HEIGHT = 200d;
    public static final int EXPECTED_FEET_SIZE = 44;

    private Person person;

    @Test
    public void whenCreatePersonWithValidAgeThenAgeIsSet() {
        person = createPersonWithAgeAndName(EXPECTED_AGE, ANDREW);
        assertEquals(EXPECTED_AGE, person.getAge());
    }

    @Test
    public void whenCreatePersonWithValidNameThenNameIsSet() {
        person = createPersonWithAgeAndName(EXPECTED_AGE, ANDREW);
        assertEquals(ANDREW, person.getName());
    }

    @Test
    public void whenCreatePersonWithValidHeightThenHeightIsSet() {
        person = new Person.Builder(EXPECTED_AGE, ANDREW).height(EXPECTED_HEIGHT).build();
        assertEquals(EXPECTED_HEIGHT, person.getHeight(), 1);
    }

    @Test
    public void whenCreatePersonWithSTshirtSizeThenSizeIsSet() {
        person = new Person.Builder(EXPECTED_AGE, ANDREW).tshirtSize(TshirtSize.S).build();
        assertEquals(TshirtSize.S, person.getTshirtSize());
    }

    @Test
    public void whenCreatePersonWithValidFeetSizeThenFeetSizeIsSet() {
        person = new Person.Builder(EXPECTED_AGE, ANDREW).feetSize(EXPECTED_FEET_SIZE).build();
        assertEquals(EXPECTED_FEET_SIZE, person.getFeetSize());
    }

    private Person createPersonWithAgeAndName(int age, String name) {
        return new Person.Builder(age, name).build();
    }
}
