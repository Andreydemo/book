package com.epam.m2.hw3;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CheckParametersForValidityTest {
    public static final int AGE = 10;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Person person;

    @Before
    public void setUp() {
        exception.expect(IllegalArgumentException.class);
    }

    @Test
    public void whenSetLessThanZeroAgeThenIllegalArgumentException() {
        exception.expectMessage("Age has to be higher than zero");
        person = new Person.Builder(-9, "").build();
    }

    @Test
    public void whnSetNullNameThenIllegalArgumentException() {
        exception.expectMessage("Name cannot be null or empty");
        person = new Person.Builder(AGE, null).build();
    }

    @Test
    public void whnSetEmptyNameThenIllegalArgumentException() {
        exception.expectMessage("Name cannot be null or empty");
        person = new Person.Builder(AGE, "").build();
    }

    @Test
    public void whnSetHeightLessThenZeroThenIllegalArgumentException() {
        exception.expectMessage("Height has to be higher than zero");
        person = new Person.Builder(AGE, "Andrew").height(-3d).build();
    }

    @Test
    public void whenSetZeroFeetSizeThenIllegalArgumentException() {
        exception.expectMessage("Feet size has to be higher less than zero");
        person = new Person.Builder(AGE, "Andrew").feetSize(0).build();
    }
}
