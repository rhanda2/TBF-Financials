package com.tbf;

import java.sql.*;
import java.util.logging.Logger;

public class ConnectorDB {

    public static Logger log = Logger.getLogger("error");

    /**
     * Forms a connection to the database and returns that connection
     * @return
     */
    public static Connection makeConnection() {
        String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(DRIVER_CLASS).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DatabaseInfo.url, DatabaseInfo.username, DatabaseInfo.password);
        } catch (SQLException e) {
            log.info("error in making connection");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return conn;
    }

    /**
     * Cleans up by closing the ResultSet, PreparedStatement and Connection.
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeConnections(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if(rs != null && !rs.isClosed()) {
                rs.close();
            }
            if(ps != null && !ps.isClosed()) {
                ps.close();
            }
            if(conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            log.info("error closing connections.");
            throw new RuntimeException(e);
        }
    }
}
