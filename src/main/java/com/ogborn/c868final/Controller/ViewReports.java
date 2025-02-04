package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.DAO.ContactDAO;
import com.ogborn.c868final.DAO.CustomerDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.MonthMap;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Appointment;
import com.ogborn.c868final.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The ViewReports controller handles the logic for generating and displaying various reports.
 *
 * This includes:
 * - Report 1: Appointments grouped by month and type.
 * - Report 2: Appointment schedules filtered by contact.
 * - Report 3: Total number of customers.
 *
 * The class also manages localization of UI components and user interactions for generating the reports.
 */
public class ViewReports implements Initializable {
    /**
     * Loaded resource bundle for localization.
     */
    private static ResourceBundle loadedBundle;

    //<editor-fold desc="global data members">
    /**
     * Label for the header of the screen.
     */
    public Label headerLabel;

    /**
     * Button to navigate back.
     */
    public Button backButton;

    /**
     * formats the datetime string to a more visually pleasing look
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //</editor-fold>

    //<editor-fold desc="report1 data members">
    /**
     * Tab displaying appointments by month and type.
     */
    public Tab appointmentsByMonthTypeTab;

    /**
     * Label displaying the title for Report 1 (Appointments by Month and Type).
     */
    public Label report1TitleLabel;

    /**
     * Button to generate report 1.
     */
    public Button report1GenerateButton;

    /**
     * Label for selecting a month in report 1.
     */
    public Label report1MonthLabel;

    /**
     * ComboBox for selecting a month in report 1.
     */
    public ComboBox<String> report1MonthComboBox;

    /**
     * Label for selecting a type in report 1.
     */
    public Label report1TypeLabel;

    /**
     * ComboBox for selecting a type in report 1.
     */
    public ComboBox<String> report1TypeComboBox;

    /**
     * Label displaying the total for report 1.
     */
    public Label report1TotalLabel;

    /**
     * Label displaying the value of the total in report 1.
     */
    public Label report1TotalValueLabel;

    /**
     * label to show the phrase "last generated :" in the current language
     */
    public Label report1TimestampLabel;

    /**
     *  label to show the timestamp
     */
    public Label report1TimeLabel;

    //</editor-fold>
    //<editor-fold desc="report2 data members">
    /**
     * Tab displaying the contact schedule.
     */
    public Tab contactScheduleTab;

    /**
     * Label displaying the title for Report 2 (Contact Schedule).
     */
    public Label report2TitleLabel;

    /**
     * TableView displaying appointments for report 2.
     */
    public TableView<Appointment> report2AppointmentTable;

    /**
     * TableColumn displaying appointment IDs in report 2.
     */
    public TableColumn<Appointment, Integer> report2AppointmentIdColumn;

    /**
     * TableColumn displaying titles in report 2.
     */
    public TableColumn<Appointment, String> report2TitleColumn;

    /**
     * TableColumn displaying descriptions in report 2.
     */
    public TableColumn<Appointment, String> report2DescriptionColumn;

    /**
     * TableColumn displaying types in report 2.
     */
    public TableColumn<Appointment, String> report2TypeColumn;

    /**
     * TableColumn displaying start times in report 2.
     */
    public TableColumn<Appointment, LocalDateTime> report2StartColumn;

    /**
     * TableColumn displaying end times in report 2.
     */
    public TableColumn<Appointment, LocalDateTime> report2EndColumn;

    /**
     * TableColumn displaying customer IDs in report 2.
     */
    public TableColumn<Appointment, Integer> report2CustomerIdColumn;

    /**
     * Label prompting contact selection in report 2.
     */
    public Label report2SelectLabel;

    /**
     * ComboBox for selecting a contact in report 2.
     */
    public ComboBox<Contact> report2SelectContactComboBox;

    /**
     * Button to generate report 2.
     */
    public Button report2GenerateButton;

    /**
     * label to show the phrase "last generated :" in the current language
     */
    public Label report2TimeStampLabel;

    /**
     *  label to show the timestamp
     */
    public Label report2TimeLabel;

    //</editor-fold>
    //<editor-fold desc="report3 data members">
    /**
     * Tab displaying the customer breakdown.
     */
    public Tab totalCustomersTab;

    /**
     * Label displaying the title for Report 3 (Total Customers Breakdown).
     */
    public Label report3TitleLabel;

    /**
     * Label displaying the total customers in report 3.
     */
    public Label report3TotalLabel;

    /**
     * Label displaying the value of total customers in report 3.
     */
    public Label report3TotalValueLabel;

    /**
     * Button to generate report 3.
     */
    public Button report3GenerateButton;

    /**
     * ListView displaying data in report 3.
     */
    public ListView<String> report3ListView;

    /**
     * label to show the phrase "last generated :" in the current language
     */
    public Label report3TimeStampLabel;

    /**
     *  label to show the timestamp
     */
    public Label report3TimeLabel;

    //</editor-fold>

    /**
     * returns to the main form
     * @param actionEvent the main menu button is clicked
     */
    public void onBackButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/MainMenuForm.fxml", actionEvent);
    }

    /**
     * Generates a report based on the selected month and appointment type.
     * The method retrieves the selected values from the ComboBoxes and queries the database for matching appointments.
     *
     * If no selection is made (impossible here since ComboBoxes default to the top option), then the button click is ignored (does nothing).
     *
     * @param actionEvent The event triggered by the button click.
     * @throws Exception If the query to the database fails.
     */
    public void onReport1GenerateButtonClick(ActionEvent actionEvent) throws Exception {
        String selectedMonth = report1MonthComboBox.getSelectionModel().getSelectedItem();
        String selectedType = report1TypeComboBox.getSelectionModel().getSelectedItem();

        if (selectedMonth != null && selectedType != null) {
            int monthIndex = -1;
            for (Map.Entry<Integer, String> entry : MonthMap.getMonthMap().entrySet()) {
                if (entry.getValue().equals(selectedMonth)) {
                    monthIndex = entry.getKey();
                    break;
                }
            }
            int count = AppointmentDAO.getMonthTypeCount(monthIndex, selectedType);
            report1TotalLabel.setText(loadedBundle.getString("report1TotalLabel"));
            report1TotalValueLabel.setText(String.valueOf(count));
            report1TimestampLabel.setText(loadedBundle.getString("generatedTime"));
            report1TimeLabel.setText(LocalDateTime.now().format(formatter));
        }
        // else do nothing because nothing is selected, or only one combobox has a selection
    }

    /**
     * Filters and displays appointments for the selected contact in the TableView.
     *
     * This method uses a lambda to filter appointments from the database:
     * - The lambda `appointment -> appointment.getContactId() == selectedContact.getContactId()` compares the selected contact's ID with each appointment's contact ID. This lambda is an implementation of the 'Predicate' functional interface, which tests a condition on each element of a collection or stream.
     * - Only matching appointments are shown in the TableView.
     *
     * The ComboBox guarantees a valid contact due to `selectFirst()` during initialization.
     *
     * @param actionEvent The event triggered by the button click.
     */
    public void onReport2GenerateButtonClick(ActionEvent actionEvent) {
        Contact selectedContact = report2SelectContactComboBox.getSelectionModel().getSelectedItem(); //always is non-null because of selectFirst() and we dont provide a way to delete a contact
        report2AppointmentTable.setVisible(true);
        report2AppointmentTable.setItems(AppointmentDAO.getAllAppointments().filtered(appointment -> appointment.getContactId() == selectedContact.getContactId()));
        report2TimeStampLabel.setText(loadedBundle.getString("generatedTime"));
        report2TimeLabel.setText(LocalDateTime.now().format(formatter));
    }

    /**
     * Displays a breakdown of the number of customers in each division that has at least one customer
     * displays the results in a list view along with the corresponding country
     * Displays the total number of customers in the system.
     * The method queries the database for all customers and sets the count in the corresponding label.
     *
     * @param actionEvent the generate button is clicked
     * @throws SQLException any database error if thrown
     */
    public void onReport3GenerateButtonClick(ActionEvent actionEvent) throws SQLException {
        report3TotalLabel.setText(loadedBundle.getString("report3TotalLabel"));
        report3TotalValueLabel.setText(String.valueOf(CustomerDAO.getAllCustomers().size()));
        ObservableList<String> customerBreakdown = CustomerDAO.getCustomerBreakdown();
        report3ListView.setItems(customerBreakdown);
        report3ListView.setVisible(true);
        report3TimeStampLabel.setText(loadedBundle.getString("generatedTime"));
        report3TimeLabel.setText(LocalDateTime.now().format(formatter));
    }

    /**
     * Initializes the ViewReports controller.
     *
     * This method performs the following setup:
     * - Loads localized strings for all labels, buttons, and table columns.
     * - Populates ComboBoxes for months and types with valid options, defaulting to the top option.
     * - Sets up the TableView for Report 2 with column mappings.
     *
     * Any errors during initialization result in a runtime exception.
     *
     * @param url The location used to resolve relative paths for the root object, or null.
     * @param resourceBundle The resources used to localize the root object, or null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/ViewReports_" + Session.getCurrentLocale().getLanguage());
            MonthMap.loadMonthMap();

            report1TypeComboBox.setItems(AppointmentDAO.getAppointmentTypes());
            report1TypeComboBox.getSelectionModel().selectFirst();
            // monthMap is automapped to locale
            report1MonthComboBox.setItems(FXCollections.observableArrayList(MonthMap.getMonthMap().values()));
            report1MonthComboBox.getSelectionModel().selectFirst();
            //</editor-fold>
            //<editor-fold desc="report2 init">
            report2AppointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            report2TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            report2DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            report2TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            report2StartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            report2EndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            report2CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            report2SelectContactComboBox.setItems(ContactDAO.getAllContacts());
            report2SelectContactComboBox.getSelectionModel().selectFirst();
            //</editor-fold>

            //<editor-fold> desc="localization brouhaha"
            headerLabel.setText(loadedBundle.getString("header"));
            backButton.setText(loadedBundle.getString("backButton"));

            // Report 1: Appointments by Month/Type
            appointmentsByMonthTypeTab.setText(loadedBundle.getString("appointmentsByMonthTypeTab"));
            report1TitleLabel.setText(loadedBundle.getString("report1TitleLabel"));
            report1GenerateButton.setText(loadedBundle.getString("report1GenerateButtonText"));
            report1MonthLabel.setText(loadedBundle.getString("report1MonthLabel"));
            report1TypeLabel.setText(loadedBundle.getString("report1TypeLabel"));

            // Report 2: Contact Schedule
            contactScheduleTab.setText(loadedBundle.getString("contactScheduleTab"));
            report2TitleLabel.setText(loadedBundle.getString("report2TitleLabel"));
            report2GenerateButton.setText(loadedBundle.getString("report2GenerateButtonText"));
            report2SelectLabel.setText(loadedBundle.getString("report2SelectLabel"));

            // Report 2: Table column headers
            report2AppointmentIdColumn.setText(loadedBundle.getString("report2AppointmentIdColumn"));
            report2TitleColumn.setText(loadedBundle.getString("report2TitleColumn"));
            report2DescriptionColumn.setText(loadedBundle.getString("report2DescriptionColumn"));
            report2TypeColumn.setText(loadedBundle.getString("report2TypeColumn"));
            report2StartColumn.setText(loadedBundle.getString("report2StartColumn"));
            report2EndColumn.setText(loadedBundle.getString("report2EndColumn"));
            report2CustomerIdColumn.setText(loadedBundle.getString("report2CustomerIdColumn"));

            // Report 3: Total Number of Customers
            totalCustomersTab.setText(loadedBundle.getString("totalCustomersTab"));
            report3TitleLabel.setText(loadedBundle.getString("report3TitleLabel"));
            report3GenerateButton.setText(loadedBundle.getString("report3GenerateButtonText"));
            //</editor-fold>
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
