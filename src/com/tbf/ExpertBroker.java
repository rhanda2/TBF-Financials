package com.tbf;

import java.util.ArrayList;

/**
 * This class models an expert broker.
 */
public class ExpertBroker extends Person implements Broker {

    private static String secId;

    public ExpertBroker(String personCode,String secId, String firstName, String lastName, Address address, ArrayList<String> emails) {
        super(personCode, firstName, lastName, address, emails);
        this.secId = secId;
    }

    public static String getSecId() {
        return secId;
    }

    @Override
    public double getCommission() {
        return 0.0375;
    }

    @Override
    public double getFees() {
        return 0.0;
    }

}
