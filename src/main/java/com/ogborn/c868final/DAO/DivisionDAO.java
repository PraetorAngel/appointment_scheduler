package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * handles database access and operations involving first-level division objects
 */
public class DivisionDAO {

    /**
     * gets the list of all the countries in the land of the free home of the brave doncha know.
     * kinda cringe the sql has a hardcoded number
     * probably best to fix that, but we'll see
     * @return the list of countries
     * @throws SQLException sql exception handling
     * @throws Exception handling for everything else
     */
    public static ObservableList<Division> getUSDivisions() throws SQLException, Exception {
        ObservableList<Division> USDivisions = FXCollections.observableArrayList();

        String getUSDivisionsSQL = "SELECT * from first_level_divisions where COUNTRY_ID = 1";
        PreparedStatement getUSDivisionsStatement = JDBC.getConnection().prepareStatement(getUSDivisionsSQL);

        ResultSet resultSet = getUSDivisionsStatement.executeQuery();

        while (resultSet.next()) {
            int divisionID = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryID = resultSet.getInt("COUNTRY_ID");

            Division nextDivision = new Division(divisionID, divisionName, countryID);
            USDivisions.add(nextDivision);
        }
        return USDivisions;
    }
    /**
     * gets the list of all the countries in the UK. kinda cringe the sql has a hardcoded number
     * probably best to fix that, but we'll see
     * @return the list of countries
     * @throws SQLException sql exception handling
     * @throws Exception handling for everything else
     */
    public static ObservableList<Division> getUKDivisions() throws SQLException, Exception {
        ObservableList<Division> UKDivisions = FXCollections.observableArrayList();

        String getUKDivisionSQL = "SELECT * from first_level_divisions where COUNTRY_ID = 2";
        PreparedStatement getUKDivisionsStatement = JDBC.getConnection().prepareStatement(getUKDivisionSQL);

        ResultSet resultSet = getUKDivisionsStatement.executeQuery();
        while (resultSet.next()) {
            int divisionID = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryID = resultSet.getInt("COUNTRY_ID");

            Division nextDivision = new Division(divisionID, divisionName, countryID);
            UKDivisions.add(nextDivision);
        }

        return UKDivisions;
    }

    /**
     * gets the list of all the countries in el calle norte. kinda cringe the sql has a hardcoded number
     * probably best to fix that, but we'll see
     * @return the list of countries
     * @throws SQLException sql exception handling
     * @throws Exception handling for everything else
     */
    public static ObservableList<Division> getCanadaDivisions() throws SQLException, Exception {
        ObservableList<Division> CanadaDivisions = FXCollections.observableArrayList();

        String getCanadaDivisionsSQL = "SELECT * from first_level_divisions where COUNTRY_ID = 3";
        PreparedStatement getCanadaDivisionsStatement = JDBC.getConnection().prepareStatement(getCanadaDivisionsSQL);

        ResultSet resultSet = getCanadaDivisionsStatement.executeQuery();

        while (resultSet.next()) {
            int divisionID = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryID = resultSet.getInt("COUNTRY_ID");

            Division nextDivision = new Division(divisionID, divisionName, countryID);
            CanadaDivisions.add(nextDivision);
        }

        return CanadaDivisions;
    }
}
