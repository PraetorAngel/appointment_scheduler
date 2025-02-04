package com.ogborn.c868final.Model;

/**
 * Represents a first-level division (e.g., state, province) with an ID, name, and associated country ID.
 * Provides methods for accessing and modifying division details.
 * Overrides {@code toString} for meaningful string representation of the division.
 */
public class Division {
    //<editor-fold desc="data members">
    /**
     * unique identifier for the division
     */
    private int divisionId;

    /**
     * name of the division
     */
    private String divisionName;

    /**
     * unique identifier for the country
     */
    private int countryId;
    //</editor-fold>

    /**
     * Constructs a new Division with the specified ID, name, and associated country ID.
     *
     * @param divisionId   The unique identifier of the division.
     * @param divisionName The name of the division.
     * @param countryId    The ID of the associated country.
     */
    public Division(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * Gets the division's unique identifier.
     *
     * @return The division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Gets the name of the division.
     *
     * @return The division's name.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Gets the country ID associated with this division.
     *
     * @return The associated country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the division's unique identifier.
     *
     * @param divisionId The new division ID.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Sets the name of the division.
     *
     * @param divisionName The new name of the division.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Sets the country ID associated with this division.
     *
     * @param countryId The new associated country ID.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Returns the division's name as its string representation.
     *
     * @return The name of the division.
     */
    @Override
    public String toString() {
        return divisionName;
    }
}
