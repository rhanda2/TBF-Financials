package com.tbf;

import java.util.ArrayList;

/**
 * This class models a normal consumer who owns a portfolio.
 */
public class Consumer extends Person {

    public Consumer(String personCode, String firstName, String lastName, Address address, ArrayList<String> emails) {
        super(personCode, firstName, lastName, address, emails);
    }

}
