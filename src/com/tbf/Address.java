package com.tbf;

/**
 * This class models an address.
 */
public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    public Address(String street, String city, String state, String zip, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    @Override
    public String toString() {
        return street + '\n' + city + ", "  + state + " " + country + " " + zip;
    }
}
