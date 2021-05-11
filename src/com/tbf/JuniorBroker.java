package com.tbf;

import java.util.ArrayList;

/**
 * This class models a junior broker.
 */
public class JuniorBroker extends Person implements Broker {

    private static String secId;

    public JuniorBroker(String personCode, String secId, String firstName, String lastName, Address address, ArrayList<String> emails) {
        super(personCode, firstName, lastName, address, emails);
        this.secId = secId;
    }

    @Override
    public double getCommission() {
        return 0.0125;
    }

    @Override
    public double getFees() {
        return (75.00);
    }
}
