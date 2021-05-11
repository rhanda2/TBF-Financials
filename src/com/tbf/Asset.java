package com.tbf;

/**
 * This class models an Asset
 */
public abstract class Asset {
    protected String code;
    protected String label;

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    protected Asset(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public abstract double getAnnualReturn();
    public abstract double getRiskMeasure();
    public abstract double getRateOfReturn();
    public abstract double getValue();
    public abstract void setExtraQuantity(Double x);

    @Override
    public String toString() {
        return String.format("%-20s %-50s %.2f%%    %-10.2f    $%-18.2f $%-18.2f", this.code, this.label, this.getRateOfReturn()*100, this.getRiskMeasure(), this.getAnnualReturn(), this.getValue());
    }
}
