package com.tbf;

import java.util.ArrayList;

/**
 * This class models a person.
 */
public abstract class Person {

    protected String personCode;
    protected String firstName;
    protected String lastName;
    protected Address address;
    protected ArrayList<String> emails;


    /**
     *
     * @param personCode
     * @param firstName
     * @param lastName
     * @param address
     * @param emails
     */
    protected Person(String personCode,String firstName, String lastName, Address address, ArrayList<String> emails) {
        this.personCode = personCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emails = emails;
    }

    public String getPersonCode() {
        return personCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

}