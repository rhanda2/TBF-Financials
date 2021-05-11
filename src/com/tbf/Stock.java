package com.tbf;

/**
 * This class models a stock asset.
 */
public class Stock extends Asset{

    private double quarterlyDividend;
    private double baseRateOfReturn;
    private double betaMeasure;
    private String stockSymbol;
    private double sharePrice;
    private double numShares = 0;

    public double getQuarterlyDividend() {
        return quarterlyDividend;
    }

    public double getBaseRateOfReturn() {
        return baseRateOfReturn;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getSharePrice() {
        return sharePrice;
    }


    /**
     *
     * @param code
     * @param label
     * @param quarterlyDividend
     * @param baseRateOfReturn
     * @param betaMeasure
     * @param stockSymbol
     * @param sharePrice
     */
    public Stock(String code, String label, double quarterlyDividend, double baseRateOfReturn, double betaMeasure, String stockSymbol, double sharePrice) {
        super(code, label);
        this.quarterlyDividend = quarterlyDividend;
        this.baseRateOfReturn = baseRateOfReturn;
        this.betaMeasure = betaMeasure;
        this.stockSymbol = stockSymbol;
        this.sharePrice = sharePrice;
    }

    public double getAnnualReturn() {
        return numShares*(this.getBaseRateOfReturn()*this.getSharePrice()/100 + (this.getQuarterlyDividend()*4));
    }

    @Override
    public double getRiskMeasure() {
        return this.betaMeasure;
    }

    @Override
    public double getRateOfReturn() {
        return (this.getAnnualReturn()/this.getValue());
    }

    @Override
    public double getValue() {
        return this.numShares*this.sharePrice;
    }

    @Override
    public void setExtraQuantity(Double x) {
        this.numShares = x;
    }
}
