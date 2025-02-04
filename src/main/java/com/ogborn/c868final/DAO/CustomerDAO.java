package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * handles database access and operations dealing with customer objects
 */
public class CustomerDAO {

    /**
     * adds a customer to the database
     * @param customerName the customer's name
     * @param address the customer's address
     * @param postalCode the zip code
     * @param phone the customer's phone number
     * @param divisionId the state or province
     * @throws SQLException less code-y than a try catch block
     */
    public static void addCustomer(String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {
            String addCustomerSQL = "INSERT INTO customers VALUES (NULL, ?, ? ,? ,?, NOW(), ?, NOW(), ?, ?)";
            PreparedStatement addCustomerStatement = JDBC.getConnection().prepareStatement(addCustomerSQL);

            addCustomerStatement.setString(1, customerName);
            addCustomerStatement.setString(2, address);
            addCustomerStatement.setString(3, postalCode);
            addCustomerStatement.setString(4, phone);
            //create_date NOW()
            addCustomerStatement.setString(5, Session.getCurrentUser().getUsername()); //created_BY
            //last_update NOW()
            addCustomerStatement.setString(6, Session.getCurrentUser().getUsername());
            addCustomerStatement.setInt(7, divisionId);

            addCustomerStatement.execute();
    }

    /**
     * updates a customer in the database
     * @param customerName the customer's name
     * @param address the customer's address
     * @param postalCode the zip code
     * @param phone the customer's phone number
     * @param divisionId the state or province
     * @param customerId the customer id
     * @throws SQLException less code-y than a try catch block
     */
    public static void updateCustomer(String customerName, String address, String postalCode, String phone, int divisionId, int customerId) throws SQLException {
            String updateCustomerSQL = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_Id = ?, Last_Update = NOW(), Last_Updated_By = ? WHERE Customer_Id = ?";

            PreparedStatement updateCustomerStatement = JDBC.getConnection().prepareStatement(updateCustomerSQL);

            updateCustomerStatement.setString(1, customerName);
            updateCustomerStatement.setString(2, address);
            updateCustomerStatement.setString(3, postalCode);
            updateCustomerStatement.setString(4, phone);
            updateCustomerStatement.setInt(5, divisionId);
            updateCustomerStatement.setString(6, Session.getCurrentUser().getUsername());
            updateCustomerStatement.setInt(7, customerId);

            updateCustomerStatement.execute();
    }

    /**
     * deletes a customer from the database
     * in order to delete the customer, all appointments must be deleted first
     * so this method checks for and deletes customer's appointments and then proceeds to delete the customer
     * @param customerId the customer id number in the database. used in lieu of customer name in case of multiple same name
     * @throws SQLException less code-y than a try catch block
     */
    public static void deleteCustomer(int customerId) throws SQLException {
        //due to foreign key constraints we have to delete the customer appointments before deleting the customer
        String deleteCustomerAppointmentsSQL = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement deleteCustomerAppointmentsStatement = JDBC.getConnection().prepareStatement(deleteCustomerAppointmentsSQL);

        deleteCustomerAppointmentsStatement.setInt(1, customerId);
        deleteCustomerAppointmentsStatement.execute();

        String deleteCustomerSQL = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement deleteCustomerStatement = JDBC.getConnection().prepareStatement(deleteCustomerSQL);

        deleteCustomerStatement.setInt(1, customerId);
        deleteCustomerStatement.execute();
    }

    /** gets a list of all customers in the database
     * @return the list of customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();

        try{
            String getAllCustomersSQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Country_ID, first_level_divisions.Division FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";


            PreparedStatement getAllCustomersStatement = JDBC.getConnection().prepareStatement(getAllCustomersSQL);
            ResultSet resultSet = getAllCustomersStatement.executeQuery();

            while (resultSet.next())
            {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String customerPhone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");

                Customer customer = new Customer(customerId, customerName, customerAddress, postalCode, customerPhone, divisionId, divisionName, countryId);
                allCustomersList.add(customer);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allCustomersList;
    }

    /**
     * Retrieves a breakdown of customers by country and division.
     * The result includes the country, division, and the total count of customers in each division.
     *
     * @return an ObservableList of formatted strings representing the customer breakdown.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<String> getCustomerBreakdown() throws SQLException {
        ObservableList<String> customerBreakdownList = FXCollections.observableArrayList();
        String getCustomerBreakdownSQL = "SELECT countries.Country, first_level_divisions.Division, COUNT(*) AS Total " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID " +
                "GROUP BY countries.Country, first_level_divisions.Division " +
                "HAVING COUNT(*) > 0";
        PreparedStatement getCustomerBreakdownStatement = JDBC.getConnection().prepareStatement(getCustomerBreakdownSQL);
        ResultSet rs = getCustomerBreakdownStatement.executeQuery();

        while (rs.next()) {
            String country = rs.getString("Country");
            String division = rs.getString("Division");
            int total = rs.getInt("Total");
            customerBreakdownList.add(String.format("%s - %s: %d", country, division, total));
        }
        return customerBreakdownList;
    }
}

