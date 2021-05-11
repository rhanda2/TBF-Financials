package com.tbf;

/**
 * This class models a private investment.
 */
public class PrivateInvestment extends Asset {

    private double quarterlyDividend;
    private double baseRateOfReturn;
    private double baseOmegaMeasure;
    private double totalValue;
    private double percentStake;

    public double getQuarterlyDividend() {
        return quarterlyDividend;
    }

    public double getBaseRateOfReturn() {
        return baseRateOfReturn;
    }

    public double getBaseOmegaMeasure() {
        return baseOmegaMeasure;
    }

    public double getTotalValue() {
        return totalValue;
    }


    /**
     *
     * @param code
     * @param label
     * @param quarterlyDividend
     * @param baseRateOfReturn
     * @param baseOmegaMeasure
     * @param totalValue
     */
    public PrivateInvestment(String code, String label, double quarterlyDividend, double baseRateOfReturn, double baseOmegaMeasure, double totalValue) {
        super(code, label);
        this.quarterlyDividend = quarterlyDividend;
        this.baseRateOfReturn = baseRateOfReturn;
        this.baseOmegaMeasure = baseOmegaMeasure;
        this.totalValue = totalValue;
    }

    public double getAnnualReturn() {
        return percentStake*(this.getBaseRateOfReturn()*this.getTotalValue()/100 + (this.getQuarterlyDividend()*4))/100;
    }

    public double getRiskMeasure() {
        return (this.getBaseOmegaMeasure() + Math.exp(-125500.0/this.getTotalValue()));
    }

    @Override
    public double getRateOfReturn() {
        return this.getAnnualReturn()/this.getValue();
    }

    @Override
    public double getValue() {
        return percentStake *this.getTotalValue()/100.0;
    }

    @Override
    public void setExtraQuantity(Double x) {
        this.percentStake = x*100;
    }

}
