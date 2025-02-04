package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * handles database access and operations involving countries
 */
public class CountryDAO{

    /**
     * gets all the countries in the database
     * @return the list of countries
     * @throws SQLException handling for sql exceptions
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountriesList = FXCollections.observableArrayList();

        String getAllCountriesSQL = "SELECT * FROM countries";
        PreparedStatement getAllCountriesStatement = JDBC.getConnection().prepareStatement(getAllCountriesSQL);

        ResultSet resultSet = getAllCountriesStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Country_ID");
            String countryName = resultSet.getString("Country");
            Country nextCountry = new Country(id, countryName);
            allCountriesList.add(nextCountry);
        }
        return allCountriesList;
    }
}