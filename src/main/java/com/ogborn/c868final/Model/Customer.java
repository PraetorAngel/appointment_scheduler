package com.ogborn.c868final.Model;

/**
 * describes customer objects and its methods
 */
public class Customer {
    //<editor-fold desc="data members">
    /**
     * unique identifier for the customer
     */
    private int customerId;

    /**
     * name of the customer
     */
    private String customerName;

    /**
     * address of the customer
     */
    private String address;

    /**
     * postal code of the customer's location
     */
    private String postalCode;

    /**
     * phone number of the customer
     */
    private String phone;

    /**
     * division ID associated with the customer
     */
    private int divisionId; // Foreign key to First-Level Divisions

    /**
     * name of the division associated with the customer
     */
    private String divisionName;

    /**
     * country ID associated with the customer
     */
    private int countryId;
    //</editor-fold>

    /**
     * Constructor for customer objects
     * @param customerId the customer id. should be autogenerated by the sql
     * @param customerName the customer name
     * @param address the address
     * @param postalCode the zip code
     * @param phone the phone number
     * @param divisionId the division id
     * @param divisionName the division name
     * @param countryId the country id
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String divisionName, int countryId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * Gets the customer ID.
     * @return the customer ID
     */
    public int getCustomerId() {return customerId;}

    /**
     * Gets the customer name.
     * @return the customer name
     */
    public String getCustomerName() {return customerName;}

    /**
     * Gets the address.
     * @return the address
     */
    public String getAddress() {return address;}

    /**
     * Gets the postal code.
     * @return the postal code
     */
    public String getPostalCode() {return postalCode;}

    /**
     * Gets the phone number.
     * @return the phone number
     */
    public String getPhone() {return phone;}

    /**
     * Gets the division ID.
     * @return the division ID
     */
    public int getDivisionId() {return divisionId;}

    /**
     * Gets the division name.
     * @return the division name
     */
    public String getDivisionName() {return divisionName;}

    /**
     * Gets the country ID.
     * @return the country ID
     */
    public int getCountryId() {return countryId;}

    /**
     * Sets the customer ID.
     * @param customerId the customer ID
     */
    public void setCustomerId(int customerId) {this.customerId = customerId;}

    /**
     * Sets the customer name.
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {this.customerName = customerName;}

    /**
     * Sets the address.
     * @param address the address
     */
    public void setAddress(String address) {this.address = address;}

    /**
     * Sets the postal code.
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    /**
     * Sets the phone number.
     * @param phone the phone number
     */
    public void setPhone(String phone) {this.phone = phone;}

    /**
     * Sets the division ID.
     * @param divisionId the division ID
     */
    public void setDivisionId(int divisionId) {this.divisionId = divisionId;}

    /**
     * Sets the division name.
     * @param divisionName the division name
     */
    public void setDivisionName(String divisionName) {this.divisionName = divisionName;}

    /**
     * Sets the country ID.
     * @param countryId the country ID
     */
    public void setCountryId(int countryId) {this.countryId = countryId;}

    /**
     * used to return the customer name for use in a combobox
     * @return the customer name explicitly cast to string in case it somehow wasn't
     */
    @Override
    public String toString() {
        return customerName;
    }
}
