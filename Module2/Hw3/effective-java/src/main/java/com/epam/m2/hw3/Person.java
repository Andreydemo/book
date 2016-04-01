package com.epam.m2.hw3;

public class Person {
    private int age;
    private String name;
    private double height;
    private TshirtSize tshirtSize;
    private int feetSize;

    private Person(Builder builder) {
        this.age = builder.age;
        this.name = builder.name;
        this.height = builder.height;
        this.tshirtSize = builder.tshirtSize;
        this.feetSize = builder.feetSize;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public TshirtSize getTshirtSize() {
        return tshirtSize;
    }

    public int getFeetSize() {
        return feetSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        Person anotherPerson = (Person) obj;
        if (anotherPerson.age == this.age) {
            return (anotherPerson.name.equals(this.name));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (age * 31) + name.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", tshirtSize=" + tshirtSize +
                ", feetSize=" + feetSize +
                '}';
    }

    public static class Builder {
        private int age;
        private String name;
        private double height;
        private TshirtSize tshirtSize;
        private int feetSize;

        public Builder(int age, String name) {
            if (age <= 0)
                throw new IllegalArgumentException("Age has to be higher than zero");
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Name cannot be null or empty");
            this.age = age;
            this.name = name;
        }

        public Person build() {
            return new Person(this);
        }

        public Builder height(double height) {
            if (height <= 0)
                throw new IllegalArgumentException("Height has to be higher than zero");
            this.height = height;
            return this;
        }

        public Builder tshirtSize(TshirtSize size) {
            this.tshirtSize = size;
            return this;
        }

        public Builder feetSize(int size) {
            if (size <= 0)
                throw new IllegalArgumentException("Feet size has to be higher less than zero");
            this.feetSize = size;
            return this;
        }
    }
}
