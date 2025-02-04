package com.ogborn.c868final.Model;

/**
 * Represents a country with an ID and name.
 * Provides getters and setters for accessing and modifying the country's details.
 * Overrides {@code toString} for meaningful string representation of the country.
 */
public class Country {
    //<editor-fold desc="data members">
    /**
     * unique identifier for the country
     */
    private int countryID;

    /**
     * name of the country
     */
    private String countryName;
    //</editor-fold>

    /**
     * Constructs a new Country with the specified ID and name.
     *
     * @param countryID   The unique identifier of the country.
     * @param countryName The name of the country.
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * Gets the country's unique identifier.
     *
     * @return The country ID.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Gets the name of the country.
     *
     * @return The country's name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the country's unique identifier.
     *
     * @param countryID The new country ID.
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Sets the name of the country.
     *
     * @param countryName The new name of the country.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Returns the country's name as its string representation.
     *
     * @return The name of the country.
     */
    @Override
    public String toString() {return countryName;}
}
