package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * handles database access and operations for contact objects
 */
public class ContactDAO{

    /**
     * returns a list of all contacts in the database
     * @return the list of contacts
     * @throws SQLException handles a sql exception if one is thrown
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContactsList = FXCollections.observableArrayList();

        String getAllContactsSQL = "SELECT * FROM contacts";
        PreparedStatement getAllContactsStatement = JDBC.getConnection().prepareStatement(getAllContactsSQL);

        ResultSet resultSet = getAllContactsStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Contact_ID");
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");

            Contact nextContact =  new Contact(id, name, email);
            allContactsList.add(nextContact);
        }
        return allContactsList;
    }
}
