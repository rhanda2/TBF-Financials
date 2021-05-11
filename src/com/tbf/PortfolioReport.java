package com.tbf;


/**
 * This class contains the methods to output the report of all portfolios.
 */
public class PortfolioReport {

    /**
     * This function helps print the whole Portfolio report
     * @param portfolioList
     */
    private static void printReport(MyLinkedList<Portfolio> portfolioList){
        System.out.println("Portfolio Summary Report");
        System.out.println("=======================================================================================");
        System.out.printf("%-20s %-30s %-30s  %-10s   %-10s  %-10s   %-10s   %-10s\n", "Portfolio", "Owner", "Manager", "Fees", "Commissions", "Weighted Risk", "Return", "Total");
        double totalFees = 0.0;
        double totalCommissions = 0.0;
        double totalReturns = 0.0;
        double totalValues = 0.0;
        for (Portfolio p: portfolioList) {
            System.out.println(p.toString());
            totalFees += p.getTotalFees();
            totalCommissions += p.getTotalCommissions();
            totalReturns += p.getTotalReturn();
            totalValues += p.getTotalValue();
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%80s    $%-10.2f  $%-20.2f  $%-10.2f  $%-10.2f\n", "Totals", totalFees, totalCommissions, totalReturns, totalValues);
        System.out.println();
    }


    /**
     * Uses the data in the Database to process data and print output
     */
    private static void printWithDatabase(){
        System.out.println("By owner name");
        MyLinkedList<Portfolio> portfolioList = LoaderDB.loaderForPortfolios(Portfolio.orderByOwnerLastName);
        printReport(portfolioList);

        System.out.println("By total value");
        MyLinkedList<Portfolio> portfolioList2 = LoaderDB.loaderForPortfolios(Portfolio.orderByTotalValue);
        printReport(portfolioList2);

        System.out.println("By broker");
        MyLinkedList<Portfolio> portfolioList3 = LoaderDB.loaderForPortfolios(Portfolio.orderByBroker);
        printReport(portfolioList3);

    }

    public static void main(String[] args) {

        printWithDatabase();

    }
}
