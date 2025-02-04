package com.ogborn.c868final.Helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class manages the database connection to a MySQL database.
 * It provides static methods to open, retrieve, and close the connection using predefined connection details.
 * The connection details (e.g., URL, username, password) are stored as private constants.
 *
 * It uses the MySQL JDBC driver to connect to the "client_schedule" database located at localhost.
 */
public abstract class JDBC {
    //<editor-fold desc="data members">
    /**
     * protocol for JDBC connection
     */
    private static final String protocol = "jdbc";

    /**
     * vendor for JDBC connection
     */
    private static final String vendor = ":mysql:";

    /**
     * location of the database server
     */
    private static final String location = "//localhost/";

    /**
     * name of the database
     */
    private static final String databaseName = "client_schedule";

    /**
     * full JDBC URL for database connection
     */
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL

    /**
     * driver class for MySQL connection
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference

    /**
     * username for database authentication
     */
    private static final String userName = "sqlUser"; // Username

    /**
     * password for database authentication
     */
    private static final String password = "Passw0rd!"; // Password

    /**
     * connection interface for the database
     */
    private static Connection connection;  // Connection Interface
    //</editor-fold>

    /**
     * opens the database connection using the information stored in the private data members
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * gets the open connection. the supplied class had a public data member
     * but this is poor practice as I understand. private data members
     * with public accessors is correct-er
     * @return the open connection
     */
    public static Connection getConnection(){
        return connection;
    }

    /**
     * closes the connection to prevent accidental database access after program is closed/terminated
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}