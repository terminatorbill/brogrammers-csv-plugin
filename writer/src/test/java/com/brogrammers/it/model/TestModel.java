package com.brogrammers.it.model;

public class TestModel {
    private final String firstName;
    private final String lastName;
    private final long counter;
    private final int smallerCounter;

    public TestModel(String firstName, String lastName, long counter, int smallerCounter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.counter = counter;
        this.smallerCounter = smallerCounter;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getCounter() {
        return counter;
    }

    public int getSmallerCounter() {
        return smallerCounter;
    }
}
