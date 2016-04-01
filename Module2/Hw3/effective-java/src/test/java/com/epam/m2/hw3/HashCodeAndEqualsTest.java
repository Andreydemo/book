package com.epam.m2.hw3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashCodeAndEqualsTest {

    public static final String IVAN = "Ivan";
    public static final String OLEG = "Oleg";
    public static final int TEN = 10;
    private Person person = new Person.Builder(TEN, IVAN).build();

    @Test
    public void whenComparePersonsThenEquals() {
        assertEquals(person, person);
    }

    @Test
    public void whenComparePersonWithOtherClassThenNotEquals() {
        assertNotEquals(person, new Object());
        assertNotEquals(person, "");
    }

    @Test
    public void whenComparePersonWithDifferentAgesThenNotEquals() {
        assertNotEquals(person, new Person.Builder(11, IVAN).build());
        assertNotEquals(person, new Person.Builder(1, IVAN).build());
    }

    @Test
    public void whenComparePersonWithDifferentNamesThenNoEquals() {
        assertNotEquals(person, new Person.Builder(TEN, OLEG).build());
    }

    @Test
    public void whenHashCodePersonsWithDifferentAgesThenHashCodeNotEquals() {
        Person anotherPerson = new Person.Builder(1, IVAN).build();
        assertNotEquals(person.hashCode(), anotherPerson.hashCode());
    }

    @Test
    public void whenHashCodePersonsWithDifferentNameThenHashCodeNotEquals() {
        Person anotherPerson = new Person.Builder(TEN, OLEG).build();
        assertNotEquals(person.hashCode(), anotherPerson.hashCode());
    }
}
