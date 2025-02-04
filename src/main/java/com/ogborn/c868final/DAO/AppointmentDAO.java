package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * handles database access and operations dealing with appointments
 */
public class AppointmentDAO {

    /**
     * get all appointments in the database
     * @return the list of appointments
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointmentsList = FXCollections.observableArrayList();

        try{
            String getAllAppointmentsSQL = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID " +
                    "FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID ORDER BY Appointment_ID";
            PreparedStatement getAllAppointmentsStatement = JDBC.getConnection().prepareStatement(getAllAppointmentsSQL);

            ResultSet resultSet = getAllAppointmentsStatement.executeQuery();
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                int contactID = resultSet.getInt("contacts.Contact_ID");
                String contactName = resultSet.getString("contacts.Contact_Name");
                String type = resultSet.getString("Type");
                Timestamp startDate = resultSet.getTimestamp("Start");
                Timestamp endDate = resultSet.getTimestamp("End");
                int customerID = resultSet.getInt("customers.Customer_ID");
                int userID = resultSet.getInt("users.User_ID");

                Appointment nextAppointment = new Appointment(appointmentID, title, description, location, contactID, contactName, type, startDate, endDate, customerID, userID);
                allAppointmentsList.add(nextAppointment);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allAppointmentsList;
    }

    /**
     * gets all database appointments that are within the current week
     * @return the list of appointments this week
     * @throws Exception an exception if one is thrown
     */
    public static ObservableList<Appointment> getWeeklyAppointments () throws Exception {
        ObservableList<Appointment> weeklyAppointmentsList = FXCollections.observableArrayList();

        String getWeeklyAppointmentsSQL = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID AND week(Start, 0) = week(curdate(), 0) ORDER BY Appointment_ID";
        PreparedStatement getWeeklyAppointmentsStatement = JDBC.getConnection().prepareStatement(getWeeklyAppointmentsSQL);
        ResultSet resultSet = getWeeklyAppointmentsStatement.executeQuery();

        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            int contactID = resultSet.getInt("contacts.Contact_ID");
            String contactName = resultSet.getString("contacts.Contact_Name");
            String type = resultSet.getString("Type");
            Timestamp startDate = resultSet.getTimestamp("Start");
            Timestamp endDate = resultSet.getTimestamp("End");
            int customerID = resultSet.getInt("customers.Customer_ID");
            int userID = resultSet.getInt("users.User_ID");

            Appointment nextAppointment = new Appointment(appointmentID, title, description, location, contactID, contactName, type, startDate, endDate, customerID, userID);

            weeklyAppointmentsList.add(nextAppointment);
        }
        return weeklyAppointmentsList;
    }

    /**
     * gets a list of all the appointments in the next 30 days.
     * SQL WEEK(CURRDATE()) describes week different than I understand week, so I've included both
     * @return the list of appointments
     * @throws Exception any exception thrown
     */
    public static ObservableList<Appointment> getNext7DaysAppointments() throws Exception {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID " +
                "FROM appointments, contacts, customers, users " +
                "WHERE appointments.Customer_ID = customers.Customer_ID " +
                "AND appointments.User_ID = users.User_ID " +
                "AND appointments.Contact_ID = contacts.Contact_ID " +
                "AND Start >= NOW() " +
                "AND Start < DATE_ADD(NOW(), INTERVAL 7 DAY) " +
                "ORDER BY Appointment_ID";

        PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            appointmentsList.add(new Appointment(
                    resultSet.getInt("Appointment_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Contact_Name"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID")
            ));
        }

        return appointmentsList;
    }

    /**
     * gets all database appointments that are within the current month
     * @return the list of appointments this month
     * @throws Exception an exception if one is thrown
     */
    public static ObservableList<Appointment> getMonthlyAppointments () throws Exception {
        ObservableList<Appointment> monthlyAppointmentsList = FXCollections.observableArrayList();

        String getMonthlyAppointmentsSQL = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID FROM appointments, contacts, customers, users WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID AND month(Start) = month(curdate()) ORDER BY Appointment_ID";
        PreparedStatement getMonthlyAppointmentsStatement = JDBC.getConnection().prepareStatement(getMonthlyAppointmentsSQL);
        ResultSet resultSet = getMonthlyAppointmentsStatement.executeQuery();

        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            int contactID = resultSet.getInt("contacts.Contact_ID");
            String contactName = resultSet.getString("contacts.Contact_Name");
            String type = resultSet.getString("Type");
            Timestamp startDate = resultSet.getTimestamp("Start");
            Timestamp endDate = resultSet.getTimestamp("End");
            int customerID = resultSet.getInt("customers.Customer_ID");
            int userID = resultSet.getInt("users.User_ID");

            Appointment nextAppointment = new Appointment(appointmentID, title, description, location, contactID, contactName, type, startDate, endDate, customerID, userID);
            monthlyAppointmentsList.add(nextAppointment);
        }
        return monthlyAppointmentsList;
    }

    /**
     * gets a list of all the appointments in the next 30 days.
     * SQL MONTH(CURRDATE()) describes month different than I understand month, so I've included both
     * @return the list of appointments
     * @throws Exception any exception thrown
     */
    public static ObservableList<Appointment> getNext30DaysAppointments() throws Exception {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT Appointment_ID, Title, Description, Location, contacts.Contact_ID, contacts.Contact_Name, Type, Start, End, customers.Customer_ID, users.User_ID " +
                "FROM appointments, contacts, customers, users " +
                "WHERE appointments.Customer_ID = customers.Customer_ID " +
                "AND appointments.User_ID = users.User_ID " +
                "AND appointments.Contact_ID = contacts.Contact_ID " +
                "AND Start >= NOW() " +
                "AND Start < DATE_ADD(NOW(), INTERVAL 30 DAY) " +
                "ORDER BY Appointment_ID";

        PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            appointmentsList.add(new Appointment(
                    resultSet.getInt("Appointment_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Contact_Name"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID")
            ));
        }

        return appointmentsList;
    }

    /**
     * adds a new appointment to the database
     * @param title the appointment title
     * @param description the appointment description
     * @param location the location of the meeting
     * @param type the type of appointment
     * @param startDate the start date and time
     * @param endDate the end date and time
     * @param customerID the customer for which the appointment will discuss
     * @param userID the company rep assigned to the appointment
     * @param contactID the contact with whom we will be meeting
     * @throws SQLException handling for sql exceptions
     * @throws Exception handling for everything else
     */
    public static void addAppointment(String title, String description, String location, String type, Timestamp startDate, Timestamp endDate, int customerID, int userID, int contactID) throws SQLException, Exception {
        String addAppointmentSQL = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)";

        PreparedStatement addAppointmentStatement = JDBC.getConnection().prepareStatement(addAppointmentSQL);

        addAppointmentStatement.setString(1, title); // Title
        addAppointmentStatement.setString(2, description); // Description
        addAppointmentStatement.setString(3, location); // Location
        addAppointmentStatement.setString(4, type); // Type
        addAppointmentStatement.setTimestamp(5, startDate); // Start
        addAppointmentStatement.setTimestamp(6, endDate); // End
        addAppointmentStatement.setString(7, Session.getCurrentUser().getUsername()); // Created_By
        addAppointmentStatement.setString(8, Session.getCurrentUser().getUsername()); // Last_Updated_By
        addAppointmentStatement.setInt(9, customerID); // Customer_ID
        addAppointmentStatement.setInt(10, userID); // User_ID
        addAppointmentStatement.setInt(11, contactID); // Contact_ID


        addAppointmentStatement.executeUpdate();
    }

    /**
     * updates an existing appointment within the database
     * @param title the appointment title
     * @param description the appointment description
     * @param location the location of the meeting
     * @param type the type of appointment
     * @param startDate the start date and time
     * @param endDate the end date and time
     * @param customerID the customer for which the appointment will discuss
     * @param userID the company rep assigned to the appointment
     * @param contactID the contact with whom will be meeting
     * @param appointmentID the id of the appointment being updated
     * @throws SQLException handling for sql exceptions
     * @throws Exception handling for everything else
     */
    public static void updateAppointment(String title, String description, String location, String type, Timestamp startDate, Timestamp endDate, int customerID, int userID, int contactID, int appointmentID ) throws SQLException, Exception {

        String updateAppointmentSQL = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Last_Update = NOW(), Last_Updated_By = ? WHERE Appointment_ID = ?";
        PreparedStatement updateAppointmentStatement = JDBC.getConnection().prepareStatement(updateAppointmentSQL);

        updateAppointmentStatement.setString(1, title);
        updateAppointmentStatement.setString(2, description);
        updateAppointmentStatement.setString(3, location);
        updateAppointmentStatement.setString(4, type);
        updateAppointmentStatement.setTimestamp(5, startDate);
        updateAppointmentStatement.setTimestamp(6, endDate);
        updateAppointmentStatement.setInt(7, customerID);
        updateAppointmentStatement.setInt(8, userID);
        updateAppointmentStatement.setInt(9, contactID);
        updateAppointmentStatement.setString(10, Session.getCurrentUser().getUsername());
        updateAppointmentStatement.setInt(11, appointmentID);

        updateAppointmentStatement.executeUpdate();
    }

    /**
     * deletes an appointment from the database
     * @param appointmentID the appointment to be deleted
     * @throws SQLException error talking to the database
     * @throws Exception catch all for anything else
     */
    public static void deleteAppointment(int appointmentID) throws SQLException, Exception {
        try {
            String deleteAppointmentSQL = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement deleteAppointmentStatement = JDBC.getConnection().prepareStatement(deleteAppointmentSQL);

            deleteAppointmentStatement.setInt(1, appointmentID);
            deleteAppointmentStatement.executeUpdate();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * gets all different appointment types
     * @return the list of appointment types
     * @throws SQLException if sql error
     */
    public static ObservableList<String> getAppointmentTypes() throws SQLException {
        ObservableList<String> distinctTypesList = FXCollections.observableArrayList();

        String getAppointmentTypesSQL = "SELECT DISTINCT type FROM appointments";
        PreparedStatement getAppointmentTypesStatement = JDBC.getConnection().prepareStatement(getAppointmentTypesSQL);

        ResultSet resultSet = getAppointmentTypesStatement.executeQuery();
        while (resultSet.next()) {
            distinctTypesList.add(resultSet.getString("type"));
        }
        return distinctTypesList;
    }

    /**
     * Retrieves the count of appointments for a given month and type.
     * Converts the localized month name to its numeric value for database queries.
     *
     * @param selectedMonth The localized month name selected by the user.
     * @param selectedType The type of appointment to filter by.
     * @return The count of matching appointments.
     * @throws Exception If a database error occurs.
     */
    public static int getMonthTypeCount(int selectedMonth, String selectedType) throws Exception {
        String getMonthTypeCountSQL = "SELECT COUNT(*) FROM appointments WHERE type = ? AND MONTH(start) = ?";

        PreparedStatement getMonthTypeCountStatement = JDBC.getConnection().prepareStatement(getMonthTypeCountSQL);

        getMonthTypeCountStatement.setString(1, selectedType);
        getMonthTypeCountStatement.setInt(2, selectedMonth);

        ResultSet resultSet = getMonthTypeCountStatement.executeQuery();
        // if anything at all is found, return the first thing in resultSet which will be the COUNT, otherwise the set will be empty
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    /**
     * checks if a new appointment overlaps with an existing one. Additional logic provided to
     * exclude current appointment from the check, i.e. an appointment does not conflict with itself
     * this can be checked in the following ways as used here. The SQL is hard to read, so I've explained it.
     * the new appointment starts before but ends during an existing appointment
     * (appointment.getStart() &lt;= Start AND appointment.getEnd() &gt; Start)
     * the new appointment starts during an existing appointment
     * (appointment.getStart() &gt;= Start AND appointment.getStart() &lt; End)
     * Customer_ID = appointment.getCustomerID() ensures we are only checking against the current customer's other appointments
     * Appointment_ID &lt;&gt; appointment.getAppointmentId() excludes current appointment from the check; SQL uses &lt;&gt; instead of !=
     * @param appointment the new (appointment to be scheduled) to be checked
     * @return false if no conflict exists, or true if one does
     * @throws SQLException to handle any sql exceptions
     * @throws Exception to handle any other exceptions
     */
    public static Boolean checkForConflictingAppointments (Appointment appointment) throws SQLException, Exception {

        String checkForConflictingAppointmentsSQL = "SELECT * FROM appointments WHERE ((? <= Start AND ? > Start) OR (? >= Start AND ? < End)) AND Customer_ID = ? AND Appointment_ID <> ?";
        PreparedStatement checkForConflictingAppointmentsStatement = JDBC.getConnection().prepareStatement(checkForConflictingAppointmentsSQL);

        checkForConflictingAppointmentsStatement.setTimestamp(1, appointment.getStart());
        checkForConflictingAppointmentsStatement.setTimestamp(2, appointment.getEnd());
        checkForConflictingAppointmentsStatement.setTimestamp(3, appointment.getStart());
        checkForConflictingAppointmentsStatement.setTimestamp(4, appointment.getStart());
        checkForConflictingAppointmentsStatement.setInt(5, appointment.getCustomerId());
        checkForConflictingAppointmentsStatement.setInt(6, appointment.getAppointmentId());

        ResultSet resultSet = checkForConflictingAppointmentsStatement.executeQuery();

        //noinspection LoopStatementThatDoesntLoop
        while (resultSet.next())
        {
            //if anything exists in resultSet >> then there is a conflict
            return true;
        }
        return false;
    }

    /**
     * Deletes all appointments for a specific customer from the database.
     * @param customerId The ID of the customer whose appointments are to be deleted.
     * @throws SQLException If a database error occurs.
     */
    public static void deleteAllAppointmentsForCustomer(int customerId) throws SQLException {
        String deleteAppointmentsSQL = "DELETE FROM appointments WHERE Customer_ID = ?";
        try (PreparedStatement deleteStatement = JDBC.getConnection().prepareStatement(deleteAppointmentsSQL)) {
            deleteStatement.setInt(1, customerId);
            deleteStatement.executeUpdate();
        }
    }
}
