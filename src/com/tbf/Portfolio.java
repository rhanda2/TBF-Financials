package com.tbf;

import java.util.Comparator;
import java.util.List;

/**
 * This class models a portfolio.
 */
public class Portfolio {

    private String portfolioCode;
    private Person owner;
    private Person manager;
    private Person beneficiary;
    private MyLinkedList<Asset> assetList;
    private double weightedRisk;
    private double totalCommissions;
    private double totalValue;
    private double totalFees;
    private double totalReturn;

    public void setAssetList(MyLinkedList<Asset> assetList) {
        this.assetList = assetList;
    }

    public String getPortfolioCode() {
        return portfolioCode;
    }

    public Person getOwner() {
        return owner;
    }

    public Person getManager() {
        return manager;
    }

    public Person getBeneficiary() {
        return beneficiary;
    }

    /**
     * Returns the weighted risk of all the assets associated.
     * @return
     */
    public double getWeightedRisk() {
        double weightedRisk = 0;
        for (Asset a : assetList) {
            weightedRisk += a.getRiskMeasure() * a.getValue();
        }
        if(this.getTotalValue() != 0) {
            weightedRisk = weightedRisk/this.getTotalValue();
        } else {
            weightedRisk = 0;
        }
        return weightedRisk;
    }

    /**
     * Gets total commissions of the manager.
     * @return
     */
    public double getTotalCommissions() {
        double totalCommissions = 0;
        totalCommissions = ((Broker) manager).getCommission()*this.getTotalReturn();
        return totalCommissions;
    }

    /**
     * Returns the sum of the values of all assets associated.
     * @return
     */
    public double getTotalValue() {
        double totalValue = 0;
        for (Asset a : assetList) {
            totalValue += a.getValue();
        }
        return totalValue;
    }

    /**
     * Returns the total fees for the manager.
     * @return
     */
    public double getTotalFees() {
        double totalFees = 0;
        totalFees = ((Broker) manager).getFees()*assetList.getSize();
        return totalFees;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-10s, %-20s %-10s, %-20s $%-10.2f  $%-10.2f %-10.4f  $%-10.2f  $%-10.2f", this.portfolioCode, this.owner.getLastName(), this.owner.getFirstName(), this.manager.getLastName(), this.manager.getFirstName(), getTotalFees(), getTotalCommissions(), this.getWeightedRisk(), this.getTotalReturn(), this.getTotalValue());
    }

    /**
     * Returns the sum of annual returns.
     * @return
     */
    public double getTotalReturn() {
        double totalReturn = 0;
        for (Asset a: assetList) {
            totalReturn += a.getAnnualReturn();
        }
        return totalReturn;
    }

    public MyLinkedList<Asset> getAssetList() {
        return assetList;
    }

    public Portfolio(String portfolioCode, Person owner, Person manager, Person beneficiary, MyLinkedList<Asset> assetList) {
        this.portfolioCode = portfolioCode;
        this.owner = owner;
        this.manager = manager;
        this.beneficiary = beneficiary;
        this.assetList = assetList;
    }

    /**
     * This is a comparator for lexicographical ordering through the owner name.
     * @param portfolioList
     */
    static Comparator<Portfolio> orderByOwnerLastName = new Comparator<Portfolio>() {
        @Override
        public int compare(Portfolio o1, Portfolio o2) {
            if (o1.getOwner().getLastName().equals(o2.getOwner().getLastName())) {
                return o1.getOwner().getFirstName().compareTo(o2.getOwner().getFirstName());
            } else {
                return o1.getOwner().getLastName().compareTo(o2.getOwner().getLastName());
            }
        }
    };

    /**
     * This is a comparator for ordering by total value.
     * @param portfolioList
     */
    static Comparator<Portfolio> orderByTotalValue = new Comparator<Portfolio>() {
        @Override
        public int compare(Portfolio o1, Portfolio o2) {
            if (o1.getTotalValue() > (o2.getTotalValue())) {
                return -1;
            } else if (o1.getTotalValue() < (o2.getTotalValue())){
                return 1;
            } else {
                return 0;
            }
        }
    };

    /**
     * This is a comparator for lexicographical ordering through the broker type and then by their name.
     * @param portfolioList
     */
    static Comparator<Portfolio> orderByBroker = new Comparator<Portfolio>() {
        @Override
        public int compare(Portfolio o1, Portfolio o2) {
            if (o1.getManager() instanceof JuniorBroker && o2.getManager() instanceof JuniorBroker) {
                if (o1.getManager().getLastName().equals(o2.getManager().getLastName())) {
                    return o1.getManager().getFirstName().compareTo(o2.getManager().getFirstName());
                } else {
                    return o1.getManager().getLastName().compareTo(o2.getManager().getLastName());
                }
            } else if (o1.getManager() instanceof ExpertBroker && o2.getManager() instanceof ExpertBroker) {
                if (o1.getManager().getLastName().equals(o2.getManager().getLastName())) {
                    return o1.getManager().getFirstName().compareTo(o2.getManager().getFirstName());
                } else {
                    return o1.getManager().getLastName().compareTo(o2.getManager().getLastName());
                }
            } else {
                if(o1.getManager() instanceof JuniorBroker) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    };

    /**
     * This method prints the details of a Portfolio
     */
    public void printEachPortfolio() {
        System.out.println("Portfolio " + this.getPortfolioCode());
        System.out.println("-----------------------------------------------------------");
        System.out.println("Owner\n" + this.getOwner().getLastName() + ", " + this.getOwner().getFirstName());
        if(this.getOwner() instanceof JuniorBroker){
            System.out.println("Junior Broker");
        } else if (this.getOwner() instanceof ExpertBroker){
            System.out.println("Expert Broker");
        }
        if(this.getOwner().getEmails() == null){
            System.out.println("[]");
        } else {
            System.out.println(this.getOwner().getEmails());
        }
        System.out.println(this.getOwner().address.toString());
        System.out.println("Manager " + this.getManager().getLastName() + ", " + this.getManager().getFirstName());
        if (this.getBeneficiary() != null) {
            System.out.println("Beneficiary\n" + this.getBeneficiary().getLastName() + ", " + this.getBeneficiary().getFirstName());
            if(this.getBeneficiary().emails == null){
                System.out.println("[]");
            } else {
                System.out.println(this.getBeneficiary().emails);
            }
            System.out.println(this.getBeneficiary().address.toString());
        } else {
            System.out.println("Beneficiary:\n" + "None");
        }
        System.out.println("Assets");
        System.out.printf("%-20s %-42s %-18s %-14s %-18s %s\n", "Code", "Asset", "Return Rate", "Risk", "Annual Return", "Value");
        for (Asset a : this.getAssetList()){
            System.out.println(a.toString());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%75s     %-14.4f $%-18.2f $%-18.2f\n", "Total",  this.getWeightedRisk(), this.getTotalReturn(), this.getTotalValue());
        System.out.println();
    }


}
