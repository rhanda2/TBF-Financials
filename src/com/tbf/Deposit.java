package com.tbf;

/**
 * This class models a deposit asset
 */
public class Deposit extends Asset{

    private double apr;
    private double amount;

    /**
     *
     * @param code
     * @param label
     * @param apr
     */
    public Deposit(String code, String label, double apr) {
        super(code, label);
        this.apr = apr;
    }

    /**
     * Returns the annual Return
     * @return
     */
    @Override
    public double getAnnualReturn() {
        return this.getRateOfReturn()*this.getValue();
    }

    /**
     * Returns the risk measure
     * @return
     */
    @Override
    public double getRiskMeasure() {
        return 0.0;
    }

    /**
     * Returns rate of return
     * @return
     */
    @Override
    public double getRateOfReturn() {
        return (Math.exp(this.apr/100) - 1.0)*100;
    }

    @Override
    public double getValue() {
        return this.amount;
    }

    @Override
    public void setExtraQuantity(Double x) {
        this.amount = x;
    }

}
