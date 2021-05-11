package com.tbf;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.setOut;

/**
 * This class contains methods to read the *.dat files.
 */
public class FileReader {

    /**
     * Reads in and processes the Person file info, stores it into a Map(Mapping person code to the person) of
     * Person and returns that map
     * @param fileName
     * @return
     */
    public static Map<String, Person> readPersonFile(String fileName) {
        File f = new File(fileName);
        Scanner s;
        Map<String, Person> personsMap = new HashMap<String, Person>();

        try {
            s = new Scanner(f);
            int numLines = Integer.parseInt(s.nextLine());
            for (int i = 0; i < numLines; i++) {
                String line = s.nextLine();
                if (line.charAt((line.length()) - 1) == ';') {
                    line = line + " ";
                }
                String tokens[] = line.split(";");
                String subBrokerTokens[] = tokens[1].split(",");
                String nameTokens[] = tokens[2].split(",");
                String subAddressTokens[] = tokens[3].split(",");
                Address address = new Address(subAddressTokens[0], subAddressTokens[1], subAddressTokens[2], subAddressTokens[3], subAddressTokens[4]);
                ArrayList<String> emailIdsList = new ArrayList<>();
                if(tokens[4].equals(" ")) {
                    emailIdsList = null;
                } else {
                    String[] emailIds = tokens[4].split(",");
                    Collections.addAll(emailIdsList, emailIds);
                }
                if(subBrokerTokens[0].equals("J")) {
                        JuniorBroker j = new JuniorBroker(tokens[0], subBrokerTokens[1], nameTokens[1], nameTokens[0], address, emailIdsList);
                        personsMap.put(tokens[0], j);
                } else if(subBrokerTokens[0].equals("E")) {
                        ExpertBroker e = new ExpertBroker(tokens[0], subBrokerTokens[1], nameTokens[1], nameTokens[0], address, emailIdsList);
                        personsMap.put(tokens[0], e);
                } else if(tokens[1].isEmpty()){
                        Consumer c = new Consumer(tokens[0], nameTokens[1], nameTokens[0], address, emailIdsList);
                        personsMap.put(tokens[0], c);
                }

                }
            s.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        return personsMap;
    }

    /**
     * Reads in an asset file, processes(Mapping a asset code to an Asset) and returns a map of Assets.
     * @param fileName
     * @return
     */
    public static Map<String, Asset> readAssetFile(String fileName) {
        File input = new File(fileName);
        Scanner s;
        Map<String, Asset> assetMap = new HashMap<>();

        try {
            s = new Scanner(input);
            int numLines = Integer.parseInt(s.nextLine());
            for (int i = 0; i < numLines; i++) {
                String line = s.nextLine();
                if (line.charAt((line.length()) - 1) == ';') {
                    line = line + " ";
                }
                String tokens[] = line.split(";");
                if (tokens[1].equals("S")) { //storing tokens as a new Stocks object
                    Stock stock = new Stock(tokens[0], tokens[2], Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]), tokens[6], Double.parseDouble(tokens[7]));
                    assetMap.put(tokens[0], stock);
                } else if (tokens[1].equals("D")) { //storing tokens as a new Deposits object
                    Deposit deposits1 = new Deposit(tokens[0], tokens[2], Double.parseDouble(tokens[3]));
                    assetMap.put(tokens[0], deposits1);
                } else if (tokens[1].equals("P")) {//storing tokens as a new PrivateInvestments object
                    PrivateInvestment pI = new PrivateInvestment(tokens[0], tokens[2], Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
                    assetMap.put(tokens[0], pI);
                }
            }
            s.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        return assetMap;
    }

//     Reads in the portfolio file, processes it and returns a list
     public static Map<String, Portfolio> readPortfolio(String filename) {
         File input = new File(filename);
         Scanner s;
         Map<String, Portfolio> portfolios = new HashMap<>();

         try {
             s = new Scanner(input);
             int numLines = Integer.parseInt(s.nextLine());
             for (int i = 0; i < numLines; i++) {
                 String line = s.nextLine();
                 if (line.charAt((line.length()) - 1) == ';') {
                     line = line + " ";
                 }
                 String tokens[] = line.split(";");
                 String assetsAndValues[] = tokens[4].split(",");
                 MyLinkedList<Asset> assets = new MyLinkedList<>();
                 for (String a : assetsAndValues) {
                     String subTokens[] = a.split(":");

                     try {

                         Asset asset = CheckAvailability.checkAsset(subTokens[0]);
                         if(asset != null){
                             asset.setExtraQuantity(Double.parseDouble(subTokens[1]));
                             assets.add(asset);
                         }

                     } catch (NullPointerException n) {
                         System.out.println(n);
                         exit(0);
                     }
                 }
                 Person beneficiary = CheckAvailability.checkPerson(tokens[3]);
                 Person owner;
                 Person manager;
                 try{
                     owner = CheckAvailability.checkPerson(tokens[1]);
                     manager = CheckAvailability.checkManager(tokens[2]);
                     Portfolio p = new Portfolio(tokens[0], owner, manager, beneficiary, assets);
                     portfolios.put(tokens[0], p);
                 } catch (NullPointerException n){
                     System.out.println(n);
                     exit(0);
                 }


             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
         return portfolios;
     }
                

}
