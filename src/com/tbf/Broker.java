package com.tbf;

public interface Broker {

    /**
     * Gets the rate of commission of a Broker.
     * @return
     */
    public double getCommission();

    /**
     * Gets the fees per asset for a Broker.
     * @return
     */
    public double getFees();
}
