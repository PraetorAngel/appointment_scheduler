package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.DAO.ContactDAO;
import com.ogborn.c868final.DAO.CustomerDAO;
import com.ogborn.c868final.DAO.UserDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Appointment;
import com.ogborn.c868final.Model.Contact;
import com.ogborn.c868final.Model.Customer;
import com.ogborn.c868final.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/**
 * controls my add appointment/update appointment forms
 */
public class AddAppointmentForm implements Initializable {
    /**
     * language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;

    // <editor-fold desc="data members">
    /**
     * currently loaded appointment, initialized to null
     */
    private Appointment loadedAppointment = null;

    // buttons
    /**
     * button to update appointment information
     */
    public Button updateButton;

    /**
     * button to cancel updates
     */
    public Button cancelButton;

    // labels
    /**
     * label for appointment ID
     */
    public Label appointmentIdLabel;

    /**
     * label for title
     */
    public Label titleLabel;

    /**
     * label for description
     */
    public Label descriptionLabel;

    /**
     * label for location
     */
    public Label locationLabel;

    /**
     * label for type
     */
    public Label typeLabel;

    /**
     * label for start time
     */
    public Label startLabel;

    /**
     * label for end time
     */
    public Label endLabel;

    /**
     * label for customer selection
     */
    public Label customerLabel;

    /**
     * label for user selection
     */
    public Label userLabel;

    /**
     * label for contact selection
     */
    public Label contactLabel;

    /**
     * label for header of the screen
     */
    public Label headerLabel;

    /**
     * label for date selection
     */
    public Label dateLabel;

    // combo boxes
    /**
     * combo box for selecting customer
     */
    public ComboBox<Customer> customerComboBox;

    /**
     * combo box for selecting user
     */
    public ComboBox<User> userComboBox;

    /**
     * combo box for selecting contact
     */
    public ComboBox<Contact> contactComboBox;

    // fields n such
    /**
     * text field for appointment ID
     */
    public TextField appointmentIdField;

    /**
     * text field for title
     */
    public TextField titleField;

    /**
     * text field for description
     */
    public TextField descriptionField;

    /**
     * text field for type
     */
    public TextField typeField;

    /**
     * text field for location
     */
    public TextField locationField;

    /**
     * date picker for selecting appointment date
     */
    public DatePicker datePicker;

    /**
     * combo box for selecting start time
     */
    public ComboBox<LocalTime> startTimeComboBox;

    /**
     * combo box for selecting end time
     */
    public ComboBox<LocalTime> endTimeComboBox;
    // </editor-fold>

    /**
     * Handles the action of clicking the update button, validating the form data, and adding or updating the appointment.
     *
     * @param event the action event triggered by the button click
     * @throws Exception if there is an issue during the process
     */
    public void onUpdateButtonClick(ActionEvent event) throws Exception {
        // Validate fields are filled
        if (!(titleField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                locationField.getText().isEmpty() || typeField.getText().isEmpty() ||
                datePicker.getValue() == null || startTimeComboBox.getValue() == null ||
                endTimeComboBox.getValue() == null || customerComboBox.getValue() == null ||
                contactComboBox.getValue() == null || userComboBox.getValue() == null)) {

            // Gather form data
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            LocalDate date = datePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();
            Customer customer = customerComboBox.getValue();
            Contact contact = contactComboBox.getValue();
            User user = userComboBox.getValue();

            // Convert start and end times to timestamps
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, startTime));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, endTime));

            Appointment newAppointment = new Appointment(loadedAppointment == null ? 0 : loadedAppointment.getAppointmentId(),title, description, location, contact.getContactId(),contact.getContactName(), type, startTimestamp, endTimestamp, customer.getCustomerId(), user.getUserId());

            if (AppointmentDAO.checkForConflictingAppointments(newAppointment)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(loadedBundle.getString("schedulingConflictHeader"));
                alert.setContentText(loadedBundle.getString("schedulingConflictText"));
                alert.showAndWait();
                return; // exit early
            }

            if (loadedAppointment == null) {
                // Add new appointment
                AppointmentDAO.addAppointment(title, description, location, type, startTimestamp, endTimestamp, customer.getCustomerId(), user.getUserId(), contact.getContactId());
            } else {
                // Update existing appointment
                AppointmentDAO.updateAppointment(title, description, location, type, startTimestamp, endTimestamp, customer.getCustomerId(), user.getUserId(), contact.getContactId(), loadedAppointment.getAppointmentId());
            }

            // Load the appointments view
            FormLoader.openForm("/com/ogborn/c868final/ViewAppointments.fxml", event);
        }
    }

    /**
     * closes back out of the form with no changes
     * @param actionEvent the event to be acted upon
     */
    public void onCancelButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/ViewAppointments.fxml", actionEvent);
    }

    /**
     * Populates the start time ComboBox with available time slots from 8:00 AM to 9:45 PM EST in 15-minute intervals; displayed in local time.
     */
    private void populateStartTimeComboBox() {
        startTimeComboBox.getItems().clear();

        LocalTime startMinLocal = convertToLocalTime(LocalTime.of(8, 0)); // 8:00 AM EST to local
        LocalTime startMaxLocal = convertToLocalTime(LocalTime.of(21, 45)); // 9:45 PM EST to local

        while (startMinLocal.isBefore(startMaxLocal.plusSeconds(1))) {
            startTimeComboBox.getItems().add(startMinLocal);
            startMinLocal = startMinLocal.plusMinutes(15);
        }
        startTimeComboBox.setValue(startTimeComboBox.getItems().get(0)); // Default to first start time
    }

    /**
     * Populates the end time ComboBox with available time slots starting 15 minutes after the selected start time
     * and ending at 10:00 PM in 15-minute intervals. If the current end time is invalidated by a freshly selected
     * start time, the end time is bumped accordingly. E.G. the user selects 9 start and 10 end
     * then changes start to 915, end will remain unaffected. if user changes start to 11, end time will
     * default to 1115. This should be sufficient to constrain end times to after the start time
     * but without automatically changing the end time simply because the start was changed
     *
     * @param startLocalTime the selected start time used to calculate the minimum end time
     */
    private void populateEndTimeComboBox(LocalTime startLocalTime) {
        LocalTime currentEndTime = endTimeComboBox.getValue();
        endTimeComboBox.getItems().setAll(); //refresh to remove blanks

        LocalTime endMinLocal = startLocalTime.plusMinutes(15); // Start from 15 minutes after selected start time
        LocalTime endMaxLocal = convertToLocalTime(LocalTime.of(22, 0)); // 10:00 PM EST to local

        while (endMinLocal.isBefore(endMaxLocal.plusSeconds(1))) {
            endTimeComboBox.getItems().add(endMinLocal);
            endMinLocal = endMinLocal.plusMinutes(15);
        }

        if (currentEndTime == null || currentEndTime.isBefore(startLocalTime.plusMinutes(15))) {
            endTimeComboBox.getSelectionModel().selectFirst(); // Default to first valid end time
        } else endTimeComboBox.setValue(currentEndTime);
    }

    /**
     * Converts a given time in EST to the system's local time zone.
     *
     * @param timeEST the LocalTime in EST to be converted
     * @return the LocalTime in the system's local time zone
     */
    private LocalTime convertToLocalTime(LocalTime timeEST) {
        LocalDateTime estDateTime = LocalDateTime.of(LocalDate.now(), timeEST);
        ZonedDateTime estZoned = estDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime localZoned = estZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalTime();
    }

    /**
     * Loads appointment data into the form for editing and renames the header and update button.
     * @param appointment The appointment to load.
     */
    public void loadAppointment(Appointment appointment) {
        this.loadedAppointment = appointment;
        headerLabel.setText(loadedBundle.getString("headerUpdate"));
        updateButton.setText(loadedBundle.getString("updateButtonUpdate"));

        // Populate form fields with appointment data
        appointmentIdField.setText(String.valueOf(appointment.getAppointmentId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        datePicker.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        startTimeComboBox.setValue(appointment.getStart().toLocalDateTime().toLocalTime());
        endTimeComboBox.setValue(appointment.getEnd().toLocalDateTime().toLocalTime());

        // Set customer, contact, and user combo boxes
        for (Customer customer : customerComboBox.getItems()) {
            if (customer.getCustomerId() == appointment.getCustomerId()) {
                customerComboBox.setValue(customer);
                break;
            }
        }

        for (Contact contact : contactComboBox.getItems()) {
            if (contact.getContactId() == appointment.getContactId()) {
                contactComboBox.setValue(contact);
                break;
            }
        }

        for (User user : userComboBox.getItems()) {
            if (user.getUserId() == appointment.getUserId()) {
                userComboBox.setValue(user);
                break;
            }
        }
    }

    /**
     * Initializes my AddAppointmentForm
     *
     * This method uses a lambda expression to add a listener to the start time ComboBox. The lambda
     * is a concise way to define the behavior that triggers whenever the selected start time changes.
     * Specifically, it updates the end time options dynamically based on the new start time selection.
     *
     * The lambda expression `startTimeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { ... })`:
     * - Captures changes to the selected value of the start time ComboBox.
     * - Executes the provided block of code whenever a new value (new start time) is selected.
     * - Ensures that the end time ComboBox is repopulated with valid time slots based on the updated start time.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/AddAppointmentForm_" + Session.getCurrentLocale().getLanguage());


            // we would have error checking for a production program to make sure getAllCustomers() isn't empty
            // as is I don't think we need to bother checking that the combobox is empty
            // before setting the default to the item 0 in list
            customerComboBox.setItems(CustomerDAO.getAllCustomers());
            customerComboBox.setValue(customerComboBox.getItems().get(0)); //default to top thing
            userComboBox.setItems(UserDAO.getAllUsers());
            userComboBox.setValue(userComboBox.getItems().get(0)); //default to top thing
            contactComboBox.setItems(ContactDAO.getAllContacts());
            contactComboBox.setValue(contactComboBox.getItems().get(0)); //default to top thing

            datePicker.setValue(LocalDate.now());
            populateStartTimeComboBox();
            populateEndTimeComboBox(startTimeComboBox.getItems().get(0));
            startTimeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    populateEndTimeComboBox(newValue);
                }
            });

            // init labels and buttons according to system language
            appointmentIdLabel.setText(loadedBundle.getString("appointmentIdLabel"));
            titleLabel.setText(loadedBundle.getString("titleLabel"));
            descriptionLabel.setText(loadedBundle.getString("descriptionLabel"));
            locationLabel.setText(loadedBundle.getString("locationLabel"));
            typeLabel.setText(loadedBundle.getString("typeLabel"));
            startLabel.setText(loadedBundle.getString("startLabel"));
            endLabel.setText(loadedBundle.getString("endLabel"));
            customerLabel.setText(loadedBundle.getString("customerLabel"));
            userLabel.setText(loadedBundle.getString("userLabel"));
            contactLabel.setText(loadedBundle.getString("contactLabel"));
            headerLabel.setText(loadedBundle.getString("headerAdd"));
            updateButton.setText(loadedBundle.getString("updateButtonAdd"));
            cancelButton.setText(loadedBundle.getString("cancelButton"));
            dateLabel.setText(loadedBundle.getString("dateLabel"));

        }catch (Exception e){
            throw new RuntimeException("Failed to initialize");
        }
    }
}
