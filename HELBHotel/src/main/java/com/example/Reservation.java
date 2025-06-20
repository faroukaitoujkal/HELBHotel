package com.example;

public class Reservation {

    public String firstName;
    public String lastName;
    public int numberOfPeople; // entre 1 et 4
    public boolean isSmoker; // true = fumeur
    public StayPurpose stayPurpose; // enum
    public int numberOfChildren;

    public Reservation(String firstName, String lastName, int numberOfPeople, boolean isSmoker, StayPurpose stayPurpose, int numberOfChildren) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfPeople = numberOfPeople;
        this.isSmoker = isSmoker;
        this.stayPurpose = stayPurpose;
        this.numberOfChildren = numberOfChildren;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public boolean hasChildren() {
        if (this.numberOfChildren != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + "\n" +
               "People: " + numberOfPeople + " | Children: " + numberOfChildren + "\n" +
               "Smoker: " + (isSmoker ? "Yes" : "No") + "\n" +
               "Stay purpose: " + stayPurpose.toString();
    }
}
