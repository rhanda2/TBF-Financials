package com.tbf;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.tbf.ConnectorDB.closeConnections;
import static com.tbf.ConnectorDB.makeConnection;
import static com.tbf.LoaderDB.*;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class PortfolioData {

	public static Logger log = Logger.getLogger("error");

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {

		Connection conn = makeConnection();

		String query = "SELECT personCode from Person";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				String code = rs.getString("personCode");
				removePerson(code);
			}
		} catch (SQLException e) {
			log.info("error in deleting all person records.");
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, rs);
	}

	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		removePortfoliosRelatedToPerson(personCode);
		removeEmailsOfAPerson(personCode);
		int personId = getPersonId(personCode);
		Connection conn = makeConnection();

		String query = "DELETE from Person WHERE personId = ?";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in removing a person");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Method to add a country record to the database
	 * @param country
	 */
	private static int addCountry(String country){

		Connection conn = makeConnection();

		String selectionQuery = "SELECT countryId FROM Country WHERE name = ?";

		int countryId = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement(selectionQuery);
			ps.setString(1, country);
			rs = ps.executeQuery();
			if(rs.next()){
				countryId = rs.getInt("countryId");
			} else {
				String query = "INSERT into Country (name) values (?)";

				PreparedStatement psInsert = null;

				try {
					psInsert = conn.prepareStatement(query);
					psInsert.setString(1, country);
					psInsert.executeUpdate();
					ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
					rs = ps.executeQuery();
					rs.next();
					countryId = rs.getInt("LAST_INSERT_ID()");

				} catch (SQLException e2) {
					log.info("error in adding in country");
					throw new RuntimeException(e2);
				}

				closeConnections(conn, psInsert, rs);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, rs);

		return countryId;
	}

	/**
	 * Method to add a state record to the database. It returns the stateId if the state already exists.
	 * @param state
	 */
	private static int addState(String state){

		Connection conn = makeConnection();

		String selectionQuery = "SELECT stateId FROM State WHERE name = ?";

		int stateId = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement(selectionQuery);
			ps.setString(1, state);
			rs = ps.executeQuery();
			if(rs.next()){
				stateId = rs.getInt("stateId");
			} else {
				String query = "INSERT into State (name) values (?)";

				PreparedStatement psInsert = null;

				try {
					psInsert = conn.prepareStatement(query);
					psInsert.setString(1, state);
					psInsert.executeUpdate();
					ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
					rs = ps.executeQuery();
					rs.next();
					stateId = rs.getInt("LAST_INSERT_ID()");

				} catch (SQLException e2) {
					log.info("error in adding in state");
					throw new RuntimeException(e2);
				}

				closeConnections(conn, psInsert, rs);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, rs);

		return stateId;
	}

	/**
	 * Method to add an address record to the database
	 * @param state
	 */
	public static int addAddress(String street, String city, String state, String zip, String country){

		Connection conn = makeConnection();

		String selectionQuery = "SELECT addressId from Address where street = ? and zip = ?";

		int addressId = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = conn.prepareStatement(selectionQuery);
			ps.setString(1, street);
			ps.setString(2, zip);
			rs = ps.executeQuery();
			if(rs.next()){
				addressId = rs.getInt("addressId");
			} else {
				int countryId = addCountry(country);
				int stateId = addState(state);

				String query = "insert into Address (street, city, zip, countryId, stateId) values " +
								"(?, ?, ?, ?, ?)";

				PreparedStatement psInsert = null;

				try {
					psInsert = conn.prepareStatement(query);
					psInsert.setString(1, street);
					psInsert.setString(2, city);
					psInsert.setString(3, zip);
					psInsert.setInt(4, countryId);
					psInsert.setInt(5, stateId);
					psInsert.executeUpdate();

					ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
					rs = ps.executeQuery();
					rs.next();
					addressId = rs.getInt("LAST_INSERT_ID()");
				} catch (SQLException e2) {
					log.info("error in adding an address.");
					throw new RuntimeException(e2);
				}
				closeConnections(conn, psInsert, rs);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, rs);

		return addressId;
	}

	/**
	 * Method to add a person record to the database with the provided data. The
	 * <code>brokerType</code> will either be "E" or "J" (Expert or Junior) or
	 * <code>null</code> if the person is not a broker.
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @param brokerType
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country, String brokerType, String secBrokerId) {

		int addressId = addAddress(street, city, state, zip, country);

		Connection conn = makeConnection();

		String query = "insert into Person (firstName , lastName, personType, personCode, brokerId, addressId) values " +
						"(?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, brokerType);
			ps.setString(4, personCode);
			ps.setString(5, secBrokerId);
			ps.setInt(6, addressId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding a person");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {

		Connection conn = makeConnection();
		int personId = getPersonId(personCode);

		String query = "insert into Email (personId, emailAddress) values" +
						"(?, ?)";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding a person");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Removes all asset records from the database
	 */
	public static void removeAllAssets() {
		Connection conn = makeConnection();

		String query = "SELECT code from Asset";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				String code = rs.getString("code");
				removeAsset(code);
			}
		} catch (SQLException e) {
			log.info("error in deleting all asset records.");
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, rs);
	}

	/**
	 * Removes the asset record from the database corresponding to the
	 * provided <code>assetCode</code>
	 * @param assetCode
	 */
	public static void removeAsset(String assetCode) {

		Connection conn = makeConnection();
		int assetId = getAssetId(assetCode);

		String query = "DELETE from PortfolioAsset where assetId = ?";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, assetId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error deleting PortfolioAsset.");
			throw new RuntimeException(e);
		}

		query = "DELETE from Asset where assetId = ?";


		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, assetId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in deleting asset.");
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, null);

	}

	/**
	 * Adds a deposit account asset record to the database with the
	 * provided data.
	 * @param assetCode
	 * @param label
	 * @param apr
	 */
	public static void addDepositAccount(String assetCode, String label, double apr) {

		Connection conn = makeConnection();

		String query = "insert into Asset (code, assetType, label, apr) values " +
						"(?, ?, ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "D");
			ps.setString(3, label);
			ps.setDouble(4, apr);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding Deposit Account.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Adds a private investment asset record to the database with the
	 * provided data.
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param baseOmega
	 * @param totalValue
	 */
	public static void addPrivateInvestment(String assetCode, String label, Double quarterlyDividend,
			Double baseRateOfReturn, Double baseOmega, Double totalValue) {

		Connection conn = makeConnection();

		String query = "insert into Asset (code, assetType, label, quarterlyDividend, baseRateOfReturn, baseOmegaMeasure, totalValue) values " +
						"(?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "P");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, baseOmega);
			ps.setDouble(7, totalValue);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding a Private Investment.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Adds a stock asset record to the database with the
	 * provided data.
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param beta
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, String label, Double quarterlyDividend,
			Double baseRateOfReturn, Double beta, String stockSymbol, Double sharePrice) {
		Connection conn = makeConnection();

		String query = "insert into Asset (code, assetType, label, quarterlyDividend, baseRateOfReturn, betaMeasure, stockSymbol, sharePrice) values " +
				"(?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "S");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, beta);
			ps.setString(7, stockSymbol);
			ps.setDouble(8, sharePrice);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding a Stock.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Removes all portfolio records from the database
	 */
	public static void removeAllPortfolios() {
		Connection conn = makeConnection();

		String query = "SELECT portfolioCode from Portfolio";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				String code = rs.getString("portfolioCode");
				removePortfolio(code);
			}
		} catch (SQLException e) {
			log.info("error in deleting all person records.");
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, rs);
	}

	/**
	 * Removes the portfolio record from the database corresponding to the
	 * provided <code>portfolioCode</code>
	 * @param portfolioCode
	 */
	public static void removePortfolio(String portfolioCode) {

		Connection conn = makeConnection();
		int portfolioId = getPortfolioId(portfolioCode);

		String query = "DELETE from PortfolioAsset where portfolioId = ?";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error deleting PortfolioAsset.");
			throw new RuntimeException(e);
		}

		query = "DELETE from Portfolio where portfolioId = ?";


		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, portfolioId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in deleting portfolio.");
			throw new RuntimeException(e);
		}
		closeConnections(conn, ps, null);

	}

	/**
	 * Adds a portfolio records to the database with the given data.  If the portfolio has no
	 * beneficiary, the <code>beneficiaryCode</code> will be <code>null</code>
	 * @param portfolioCode
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addPortfolio(String portfolioCode, String ownerCode, String managerCode, String beneficiaryCode) {

		Connection conn = makeConnection();
		int ownerId = getPersonId(ownerCode);
		int managerId = getPersonId(managerCode);
		int beneficiaryId = getPersonId(beneficiaryCode);

		String query = "insert into Portfolio (portfolioCode, ownerId, managerId, beneficiaryId) values " +
						"(?, ?, ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			ps.setInt(2, ownerId);
			ps.setInt(3, managerId);
			if(beneficiaryId != 0) {
				ps.setInt(4, beneficiaryId);
			} else {
				ps.setString(4, null);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("error in adding a portfolio.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}

	/**
	 * Associates the asset record corresponding to <code>assetCode</code> with the
	 * portfolio corresponding to the provided <code>portfolioCode</code>.  The third
	 * parameter, <code>value</code> is interpreted as a <i>balance</i>, <i>number of shares</i>
	 * or <i>stake percentage</i> depending on the type of asset the <code>assetCode</code> is
	 * associated with.
	 * @param portfolioCode
	 * @param assetCode
	 * @param value
	 */
	public static void addAsset(String portfolioCode, String assetCode, double value) {

		Connection conn = makeConnection();
		int assetId = getAssetId(assetCode);

		String query = "";
		query = "insert into PortfolioAsset (portfolioId, assetId, assetValue) values " +
						"((select portfolioId from Portfolio where portfolioCode = ?), ?, ?)";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			ps.setInt(2, assetId);
			ps.setDouble(3, value);
			ps.executeUpdate();
		} catch (SQLException e){
			log.info("error in adding asset.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}


	/**
	 * This method removes all portfolios related to a person.
	 * @param personCode
	 */
	private static void removePortfoliosRelatedToPerson(String personCode){

		Connection conn = makeConnection();
		int personId = getPersonId(personCode);

		//for storing the portfolioCodes belonging to a person
		List<String> portfolios = new ArrayList<>();

		String query = "SELECT po.portfolioCode from Portfolio po "  +
				"JOIN Person p on p.personId = po.ownerId " +
				"WHERE p.personId = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while(rs.next()){
				String portCode = rs.getString("portfolioCode");
				portfolios.add(portCode);
			}
		} catch (SQLException e) {
			log.info("Error in getting portfolios owned");
		}

		query = "SELECT po.portfolioCode from Portfolio po "  +
				"JOIN Person p on p.personId = po.managerId " +
				"WHERE p.personId = ?";

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while(rs.next()){
				String portCode = rs.getString("portfolioCode");
				portfolios.add(portCode);
			}
		} catch (SQLException e) {
			log.info("Error in getting portfolios managed");
		}

		query = "SELECT po.portfolioCode from Portfolio po "  +
				"JOIN Person p on p.personId = po.beneficiaryId " +
				"WHERE p.personId = ?";

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while(rs.next()){
				String portCode = rs.getString("portfolioCode");
				portfolios.add(portCode);
			}
		} catch (SQLException e) {
			log.info("Error in getting portfolios benefited from");
		}

		for(String code: portfolios) {
			removePortfolio(code);
		}
		closeConnections(conn, ps, rs);
	}

	/**
	 * This method removes the emails of a person
	 * @param personCode
	 */
	private static void removeEmailsOfAPerson(String personCode) {

		Connection conn = makeConnection();
		int personId = getPersonId(personCode);

		String query = "DELETE from Email " +
						"WHERE personId = ?";

		PreparedStatement ps = null;

		try{
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.info("Error in removing emails of a person.");
			throw new RuntimeException(e);
		}

		closeConnections(conn, ps, null);
	}
}

