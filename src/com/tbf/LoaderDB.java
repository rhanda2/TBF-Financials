package com.tbf;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static com.tbf.ConnectorDB.closeConnections;
import static com.tbf.ConnectorDB.makeConnection;

/**
 * This class contains methods to load data from the database
 */
public class LoaderDB {

    public static Logger log = Logger.getLogger("error");



    /**
     * Gets the address of a particular person from the Database.
     * @param personId
     * @return
     */
    private static Address getAddress(int personId){
        Address a = null;

        Connection conn = makeConnection();

        String query = "SELECT a.street, a.city, a.zip, s.name as state, c.name as country from Person p " +
                        "LEFT JOIN Address a on a.addressId = p.addressId " +
                        "LEFT JOIN Country c on a.countryId = c.countryId " +
                        "LEFT JOIN State s on a.stateId = s.stateId " +
                        "WHERE p.personId = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();

            while(rs.next()) {
                String street = rs.getString("street");
                String city = rs.getString("city");
                String zip = rs.getString("zip");
                String state = rs.getString("state");
                String country = rs.getString("country");
                a = new Address(street, city, state, zip, country) ;
            }
        } catch (SQLException e) {
            log.info("error in loading address.");
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);

        return a;
    }

    /**
     * Gets all the emails of a particular person from the Database.
     * @param personId
     * @return
     */
    private static ArrayList<String> getEmailList(int personId) {
        ArrayList<String> emailIdsList = new ArrayList<>();

        Connection conn = makeConnection();

        String query = "SELECT e.emailAddress from Email e " +
                        "LEFT JOIN Person p on p.personId = e.personId " +
                        "WHERE p.personId = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();

            while(rs.next()) {
                String email = rs.getString("emailAddress");
                emailIdsList.add(email);
            }
        } catch (SQLException e) {
            log.info("error in loading emails.");
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);

        return emailIdsList;
    }

    /**
     * Returns a Person Object having the particular personId
     * @param personId
     * @return
     */
    private static Person getPerson(int personId) {
        Person p = null;

        Connection conn = makeConnection();

        String query = "SELECT p.firstName, p.lastName, p.personCode, p.personType, p.brokerId, p.personId from Person p WHERE p.personId = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();

            while(rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String personCode = rs.getString("personCode");
                String personType = rs.getString("personType");
                String brokerId = rs.getString("brokerId");
                Address address = LoaderDB.getAddress(rs.getInt("personId"));
                ArrayList<String> emailIdsList = LoaderDB.getEmailList(rs.getInt("personId"));

                if(personType == null){
                    p = new Consumer(personCode, firstName, lastName, address, emailIdsList);
                } else if(personType.equals("E")) {
                    p = new ExpertBroker(personCode, brokerId, firstName, lastName, address, emailIdsList);
                } else if(personType.equals("J")) {
                    p = new JuniorBroker(personCode, brokerId, firstName, lastName, address, emailIdsList);
                }
            }
        } catch (SQLException e) {
            log.info("error in loading person.");
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);

        return p;
    }

    /**
     * Returns an Asset Object of a particular assetId
     * @param assetId
     * @return
     */
    private static Asset getAsset(int assetId) {
        Asset a = null;

        Connection conn = makeConnection();

        String query = "SELECT code, assetType, label, apr, quarterlyDividend, baseRateOfReturn, betaMeasure," +
                "stockSymbol, sharePrice, baseOmegaMeasure, totalValue from Asset " +
                "WHERE assetId = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, assetId);
            rs = ps.executeQuery();
            while(rs.next()){
                String code = rs.getString("code");
                String assetType = rs.getString("assetType");
                String label = rs.getString("label");

                if (assetType.equals("S")) { //storing tokens as a new Stocks object
                    a = new Stock(code, label, rs.getDouble("quarterlyDividend"), rs.getDouble("baseRateOfReturn")*100.0, rs.getDouble("betaMeasure"), rs.getString("stockSymbol"), rs.getDouble("sharePrice"));
                } else if (assetType.equals("D")) { //storing tokens as a new Deposits object
                    a = new Deposit(code, label, rs.getDouble("apr"));
                } else if (assetType.equals("P")) {//storing tokens as a new PrivateInvestments object
                    a = new PrivateInvestment(code, label, rs.getDouble("quarterlyDividend"), rs.getDouble("baseRateOfReturn")*100, rs.getDouble("baseOmegaMeasure"), rs.getDouble("totalValue"));
                }
            }
        } catch (SQLException e) {
            log.info("error in loading assets.");
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);

        return a;
    }

    /**
     * Creates and returns a portfolio having a particular portfolioId
     * @param portfolioId
     * @return
     */
    private static Portfolio getPortfolio(int portfolioId) {
        Portfolio p = null;

        Connection conn = makeConnection();

        String query = "SELECT ownerId, managerId, managerId, beneficiaryId, portfolioCode from Portfolio " +
                        "WHERE portfolioId = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            ps = conn.prepareStatement(query);
            ps.setInt(1, portfolioId);
            rs = ps.executeQuery();
            while(rs.next()) {
                String portfolioCode = rs.getString("portfolioCode");
                Person o = getPerson(rs.getInt("ownerId"));
                Person m = getPerson(rs.getInt("managerId"));
                int beneficiaryId = rs.getInt("beneficiaryId");
                Person b = null;
                if(beneficiaryId != 0) {
                    b = getPerson(beneficiaryId);
                }
                p = new Portfolio(portfolioCode, o, m, b, null);
            }
            rs.close();
        } catch (SQLException e) {
            log.info("error in loading portfolio");
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        MyLinkedList<Asset> assetList = new MyLinkedList<>();

        query = "SELECT a.assetId, pa.assetValue from Portfolio p " +
                "LEFT JOIN PortfolioAsset pa on pa.portfolioId = p.portfolioId " +
                "LEFT JOIN Asset a on a.assetId = pa.assetId " +
                "WHERE p.portfolioId = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, portfolioId);
            rs = ps.executeQuery();

            while(rs.next()){
                Asset a = getAsset(rs.getInt("assetId"));
                Double assetValue = rs.getDouble("assetValue");
                if(a != null) {
                    a.setExtraQuantity(assetValue);
                    assetList.add(a);
                }
            }
            p.setAssetList(assetList);
        } catch (SQLException e) {
            log.info("error in loading assets in the portfolio.");
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);

        return p;
    }

    /**
     * Returns the whole list of portfolios.
     * @return
     */
    public static MyLinkedList<Portfolio> loaderForPortfolios(Comparator comparator) {

        MyLinkedList<Portfolio> portfolios = new MyLinkedList<>(comparator);

        Connection conn = makeConnection();

        String query = "SELECT portfolioId from Portfolio";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                Portfolio p = getPortfolio(rs.getInt("portfolioId"));
                portfolios.add(p);
            }
        } catch (SQLException e) {
            log.info("error in loading portfolios.");
            System.out.println("SQLException: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        closeConnections(conn, ps, rs);
        return portfolios;
    }

    /**
     * This method returns assetId of an asset through its code.
     * @param assetCode
     * @return
     */
    public static int getAssetId(String assetCode) {

        Connection conn = makeConnection();

        String query = "SELECT assetId from Asset where code = ?";

        int assetId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, assetCode);
            rs = ps.executeQuery();
            if(rs.next()){
                assetId = rs.getInt("assetId");
            }
        } catch (SQLException e) {
            log.info("error in getting the asset Id");
            throw new RuntimeException(e);
        }
        closeConnections(conn, ps, rs);
        return assetId;
    }

    /**
     * This method returns a personId of a person through their code.
     * @param personCode
     * @return
     */
    public static int getPersonId(String personCode) {
        Connection conn = makeConnection();

        String query = "SELECT personId from Person where personCode = ?";

        int personId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, personCode);
            rs = ps.executeQuery();
            if(rs.next()){
                personId = rs.getInt("personId");
            }
        } catch (SQLException e) {
            log.info("error in getting the person Id.");
            throw new RuntimeException(e);
        }
        closeConnections(conn, ps, rs);
        return personId;
    }

    /**
     * This method returns a portfolioId of a portfolio through its code.
     * @param portfolioCode
     * @return
     */
    public static int getPortfolioId(String portfolioCode) {
        Connection conn = makeConnection();

        String query = "SELECT portfolioId from Portfolio where portfolioCode = ?";

        int portfolioId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, portfolioCode);
            rs = ps.executeQuery();
            if(rs.next()){
                portfolioId = rs.getInt("portfolioId");
            }
        } catch (SQLException e) {
            log.info("error in getting the portfolio Id.");
            throw new RuntimeException(e);
        }
        closeConnections(conn, ps, rs);
        return portfolioId;
    }
}
